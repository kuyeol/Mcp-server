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
        return user.from(userEntity);
    }

    @Transactional
    public ProviderRecord addProvider(ProviderRecord provider) {

        UserEntity ref = em.getReference(UserEntity.class, provider.userId());

        AgentProviderEntity agentProvider = provider.toAgentProviderEntity();
        agentProvider.setId("qq");
        ref.addProvider(agentProvider);
        em.persist(ref);

        return provider.from(agentProvider);
    }

    public static void main(String[] args) {

        AgentProviderEntity agentProvider  = new AgentProviderEntity();
        AgentProviderEntity agentProvider2 = new AgentProviderEntity();
        agentProvider.setName("agentProvider");
        agentProvider2.setName("agentProvider2");
        List<AgentProviderEntity> agentProviders = new ArrayList<>();
        agentProviders.add(agentProvider2);
        agentProviders.add(agentProvider);

        UserRecord userRecord = new UserRecord.Builder().setAgent(agentProviders).setId("dd").setName("naem").build();
        userRecord.agents().add(agentProvider);
        userRecord.agents().add(agentProvider2);

        ProviderDAO providerDAO = new ProviderDAO(ProviderDAO.em);
        userRecord = providerDAO.registerUser(userRecord);
        System.out.println(userRecord);

        UserEntity userEntity = new UserEntity();

    }

    //public

    // find Entity
    // updata Entity
    // delete Entity

}
