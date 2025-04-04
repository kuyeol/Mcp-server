package org.acme.panache.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

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
