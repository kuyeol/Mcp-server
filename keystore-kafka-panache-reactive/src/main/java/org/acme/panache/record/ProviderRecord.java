package org.acme.panache.record;

import org.acme.panache.entity.AgentProviderEntity;

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
