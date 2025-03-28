package org.acme.panache.entity;

import jakarta.persistence.*;
import org.acme.panache.util.IdGenerater;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
public class UserEntity
{

    @Id
    @Access(AccessType.PROPERTY)
    private String id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    @BatchSize(size = 20)
    private List<AgentProviderEntity> agentProviders = new LinkedList<>();

    private String name;

    public void setName(String userName)
    {
        this.name = userName;
    }

    public List<AgentProviderEntity> getAgentProviders()
    {
        return this.agentProviders;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId() {
        this.id = IdGenerater.create();
    }

    public void addProvider(AgentProviderEntity agentProvider)
    {
        this.agentProviders.add(agentProvider);
    }

    public void addProvider(List<AgentProviderEntity> agentProviders) {
        this.agentProviders.addAll(agentProviders);

    }

    public String getName()
    {
        return this.name;
    }




}
