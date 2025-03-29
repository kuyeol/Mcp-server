package org.acme.panache.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "findByUserName", query = "SELECT p FROM AgentProviderEntity p WHERE p.user =:user"),
        @NamedQuery(name = "findByProviderName", query = "SELECT p FROM AgentProviderEntity p WHERE p.name =:name") })

@Table(name = "AgentProviderEntity", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "BASE_URL", "NAME" }),
        //  @UniqueConstraint(columnNames = { "REALM_ID", "EMAIL_CONSTRAINT" })
})
public class AgentProviderEntity
{

    @Id
    @Access(AccessType.PROPERTY)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERENTITY_ID")
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "provider")
    @BatchSize(size = 20)
    private List<AgentEntity> agents = new ArrayList<AgentEntity>();

    @Column(name = "NAME")
    private String name;

    @Column(name = "BASE_URL")
    private String baseUrl;

    private String apiKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getName() {
        return name;
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
        return "AgentProviderEntity{" + "id='" + id + '\'' + ", user=" + user + ", agents=" + agents + ", name='" + name + '\'' + ", baseUrl='" + baseUrl + '\'' + ", apiKey='" + apiKey + '\'' + '}';
    }




}
