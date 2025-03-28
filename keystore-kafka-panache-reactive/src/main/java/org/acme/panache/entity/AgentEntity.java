package org.acme.panache.entity;

import jakarta.persistence.*;

@Entity
public class AgentEntity
{

    @Id
    @Access(AccessType.PROPERTY)
    private String chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AGENTPROVIDERENTITY_NAME")
    private AgentProviderEntity provider;

    private String role;

    private String modelName;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public AgentProviderEntity getProvider() {
        return provider;
    }

    public void setProvider(AgentProviderEntity provider) {
        this.provider = provider;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return "AgentEntity{" + "chatId='" + chatId + '\'' + ", provider=" + provider + ", role='" + role + '\'' + ", modelName='" + modelName + '\'' + '}';
    }




}
