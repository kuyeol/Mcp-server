package org.acme.panache.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.acme.panache.entity.AgentProviderEntity;
import org.acme.panache.entity.UserEntity;
import org.acme.panache.exception.ModelException;
import org.acme.panache.record.ProviderRecord;
import org.acme.panache.record.UserRecord;
import org.acme.panache.util.IdGenerater;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserDao
{

  private static EntityManager em;

  private boolean isProvider = false;

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
