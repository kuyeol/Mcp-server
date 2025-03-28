package org.acme.panache.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.panache.entity.AgentProviderEntity;
import org.acme.panache.entity.UserEntity;
import org.acme.panache.record.ProviderRecord;
import org.acme.panache.record.UserRecord;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProviderDAO
{

    private static EntityManager em;

    private boolean isProvider = false;

    public ProviderDAO(EntityManager em)
    {
        ProviderDAO.em = em;
    }

    // new Entity [ User , Provider , Agent]
    //중복은 jpa 유니크 어노테이션 사용
    @Transactional
    public UserRecord registerUser(UserRecord user) {

        UserEntity userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setName(user.name());
        em.persist(userEntity);
        return new UserRecord(userEntity.getId(),userEntity.getName());
    }

    @Transactional
    public ProviderRecord addProvider(ProviderRecord provider,String userId) {

        UserEntity ref = em.getReference(UserEntity.class, userId);

        AgentProviderEntity agentProvider = provider.toAgentProviderEntity();
        agentProvider.setId("qq");
        agentProvider.setUser(ref);
        ref.addProvider(agentProvider);
        em.persist(ref);

        return provider.from(agentProvider);
    }







    public static void main(String[] args) {

    }



    // find Entity

    public UserRecord findUser(String userId) {
        UserEntity userEntity = em.getReference(UserEntity.class, userId);


        return new UserRecord(userEntity.getId(),userEntity.getName());
    }



    // updata Entity
    // delete Entity

}
