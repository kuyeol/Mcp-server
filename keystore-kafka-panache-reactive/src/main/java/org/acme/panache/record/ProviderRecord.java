package org.acme.panache.record;

import org.acme.panache.entity.AgentProviderEntity;

public record ProviderRecord(String id, String userId, String name, String baseUrl, String apiKey)
{

    public ProviderRecord from(AgentProviderEntity provider) {
        return new ProviderRecord(provider.getId(), provider.getUser().getId(), provider.getName(),
                                  provider.getBaseUrl(), provider.getApiKey());
    }

    public AgentProviderEntity toAgentProviderEntity() {
        AgentProviderEntity agentProviderEntity = new AgentProviderEntity();
        agentProviderEntity.setId();
        agentProviderEntity.setName(name);
        agentProviderEntity.setBaseUrl(baseUrl);
        agentProviderEntity.setApiKey(apiKey);

        return agentProviderEntity;

    }




}
