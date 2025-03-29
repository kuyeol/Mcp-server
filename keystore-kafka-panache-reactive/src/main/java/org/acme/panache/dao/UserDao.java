package org.acme.panache.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
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

    public UserDao(EntityManager em)
    {
        UserDao.em = em;
    }

    public Optional<UserEntity> getFromUserId(String id) {
        return Optional.ofNullable(em.find(UserEntity.class, id));

    }

    public Optional<UserEntity> getFromUserName(String name) {

        Optional<UserEntity> userEntity = em.createNamedQuery("findByName", UserEntity.class)
                                            .setParameter("name", name)
                                            .getResultStream()
                                            .findFirst();
        return userEntity;
    }

    public UserRecord findUserByName(String name) {
        UserEntity userEntity = getFromUserName(name).orElseThrow(() -> new ModelException("User not found"));
        return UserRecord.from(userEntity);
    }

    public UserRecord findUserById(String userId) {

        UserEntity userEntity = getFromUserId(userId).orElseThrow(() -> new ModelException("User not found"));

        return UserRecord.from(userEntity);
    }

    public List<AgentProviderEntity> getFromProviderName(String name) {

        return em.createNamedQuery("findByProviderName", AgentProviderEntity.class)
                 .setParameter("name", name)
                 .getResultStream()
                 .collect(Collectors.toList());

    }

    public List<ProviderRecord> findByProviderName(String name) {

        if (getFromProviderName(name).isEmpty()) {
            throw new ModelException("Provider Not Found");
        }

        List<AgentProviderEntity> providerList = new LinkedList<>(getFromProviderName(name));

        return new LinkedList<>(providerList.stream().map(ProviderRecord :: from).toList());

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

        if (getFromUserName(name).isEmpty()) {
            throw new ModelException("User Not Found");
        }
        if (getFromUserName(name).get()
                                 .getAgentProviders()
                                 .stream()
                                 .anyMatch(p -> p.getName().equals(provider.name()) && p.getBaseUrl()
                                                                                        .equals(provider.baseUrl()))) {
            throw new ModelException("Provider  Aleady Exist");
        }

        String userId = getFromUserName(name).get().getId();

        UserEntity ref = em.getReference(UserEntity.class, userId);

        AgentProviderEntity agentProvider = provider.toAgentProviderEntity();
        agentProvider.setId(IdGenerater.create());
        agentProvider.setUser(ref);
        ref.addProvider(agentProvider);
        em.persist(ref);

        return provider.from(agentProvider);
    }

    // updata Entity
    // delete Entity

}
