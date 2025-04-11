   

```kotlin

@POST
    @Path("/user/register")
    public Response registerUser(UserRecord user)
    {
        try {
            user = providerDao.registerUser(user);
            return Response.status(Response.Status.CREATED)
                           .entity("User registered successfully.\n" + user.name())
                           .build();

        } catch (ModelException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }

    }

    @POST
    @Path("/provider/addprovider{userId}")
    public Response addProvider(
            @PathParam("userId")
            String userName, ProviderRecord provider)
    {
        try {
            provider = providerDao.addProvider(provider, userName);
            return Response.status(Response.Status.CREATED).entity("successfully.\n" + provider).build();
        } catch (ModelException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }

    }

    @DELETE
    @Path("{providerName}")
    public void deleteProvider(String userId, String providerName) {

        providerDao.deleteProvider(userId, providerName);

    }
```

```java

public record ProviderRecord(String id, String providerName, String baseUrl, String apiKey)
{

    public static ProviderRecord from(AgentProviderEntity provider)
    {
        return new ProviderRecord(provider.getId(), provider.getName(), provider.getBaseUrl(), provider.getApiKey());
    }

    public AgentProviderEntity toAgentProviderEntity()
    {
        AgentProviderEntity agentProviderEntity = new AgentProviderEntity();
        agentProviderEntity.setId(id);
        agentProviderEntity.setName(providerName);
        agentProviderEntity.setBaseUrl(baseUrl);
        agentProviderEntity.setApiKey(apiKey);

        return agentProviderEntity;

    }
    

}

```

```java

@ApplicationScoped
public class UserDao
{

  private static EntityManager em;

  public UserDao(EntityManager em) {
    UserDao.em = em;
  }

  @Transactional
  public Optional<UserEntity> getFromUserId(String id) {
    return Optional.ofNullable(em.find(UserEntity.class, id));

  }

  @Transactional
  public Optional<UserEntity> getFromUserName(String name) {

    return em.createNamedQuery("findByName", UserEntity.class)
             .setParameter("name", name)
             .getResultStream()
             .findFirst();

  }

  @Transactional
  public List<AgentProviderEntity> getProviderByUserId(String userId) {

    return em.createNamedQuery("findByUserId", AgentProviderEntity.class)
             .setLockMode(LockModeType.PESSIMISTIC_WRITE)
             .setParameter("userId", userId)
             .getResultStream()
             .collect(Collectors.toList());

  }

  public List<ProviderRecord> findProviderByUserId(String userId) {

    List<AgentProviderEntity> providers = new LinkedList<>(getProviderByUserId(userId));

    return new LinkedList<>(providers.stream()
                                     .map(ProviderRecord :: from)
                                     .toList());
  }

  public UserRecord findUserByName(String name) {
    UserEntity userEntity = getFromUserName(name).orElseThrow(() -> new ModelException("User not found"));
    return UserRecord.from(userEntity);
  }

  public UserRecord findUserById(String userId) {

    UserEntity userEntity = getFromUserId(userId).orElseThrow(() -> new ModelException("User not found"));

    return UserRecord.from(userEntity);
  }

  @Transactional
  public UserRecord registerUser(UserRecord user) {

    if (getFromUserName(user.name()).isPresent()) {
      throw new ModelException("UserName Aleady Exist");
    }
    if (getFromUserId(user.id()).isPresent()) {
      throw new ModelException("User Aleady Exist");
    }
    UserEntity userEntity = new UserEntity();
    userEntity.setId(IdGenerater.create());
    userEntity.setName(user.name());
    em.persist(userEntity);
    return UserRecord.from(userEntity);

  }

  public UserRecord updateUser(String id) {
    UserEntity userEntity = em.getReference(UserEntity.class, id);

    return new UserRecord(userEntity.getId(), userEntity.getName());
  }

  @Transactional
  public ProviderRecord addProvider(ProviderRecord provider, String name) {

    UserEntity user = getFromUserName(name).orElseThrow(() -> new ModelException("User not found"));

    if (doesProviderExist(user.getId(), provider.providerName(), provider.baseUrl())) {
      throw new ModelException(
              // 구체적 예외 및 메시지
              String.format("Provider with name '%s' and URL '%s' already exists for user '%s'",
                            provider.providerName(), provider.baseUrl(), name));
    }

    AgentProviderEntity agentProvider = provider.toAgentProviderEntity();

    agentProvider.setId(IdGenerater.create());
    agentProvider.setUser(user);
    user.addProvider(agentProvider);
    em.persist(agentProvider);
    return ProviderRecord.from(agentProvider);
  }

  @Transactional
  public String deleteProvider(String userId, String providerId) {

    AgentProviderEntity provider = getProviderByUserId(userId).stream()
                                                              .filter(p -> p.getId()
                                                                                .equals(providerId))
                                                              .findFirst()
                                                              .orElse(null);


    em.remove(provider);

    return provider.getName();

  }

  private boolean doesProviderExist(String userId, String providerName, String baseUrl) {
    String jpql = "SELECT COUNT(p) FROM AgentProviderEntity p " +
                  "WHERE p.user.id = :userId AND p.name = :providerName AND p.baseUrl = :baseUrl";

    TypedQuery<Long> query = em.createQuery(jpql, Long.class);
    query.setParameter("userId", userId);
    query.setParameter("providerName", providerName);
    query.setParameter("baseUrl", baseUrl);
    return query.getSingleResult() > 0;
  }

  // updata Entity
  // delete Entity

}

```

```java

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "findByUserId", query = "SELECT DISTINCT p FROM AgentProviderEntity p WHERE p.userId =: userId") })

public class AgentProviderEntity
{

    @Id
    @Access(AccessType.PROPERTY)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY ,optional = false)
    @JoinColumn(name = "UserEntity_ID" , nullable = false)
    private UserEntity user;

    @Column(name = "userId",nullable = false)
    private String userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "provider", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<AgentEntity> agents = new ArrayList<AgentEntity>();

    @Column(name = "NAME")
    private String name;

    @Column(name = "BASE_URL")
    private String baseUrl;

    private String apiKey;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        this.setUserId(user.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String providerName) {
        this.name = providerName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<AgentEntity> getAgents() {
        return agents;
    }

    public void setAgents(List<AgentEntity> agents) {
        this.agents = agents;
    }

    public void addAgent(AgentEntity agent) {
        agents.add(agent);
    }

    @Override
    public String toString() {
        return "AgentProviderEntity{" + "id='" + id + '\'' + ", user=" + user + ", agents=" + agents + ", name='" +
               name + '\'' + ", baseUrl='" + baseUrl + '\'' + ", apiKey='" + apiKey + '\'' + '}';
    }
    

}

```

