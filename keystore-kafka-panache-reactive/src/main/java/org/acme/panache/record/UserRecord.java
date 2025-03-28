package org.acme.panache.record;

import org.acme.panache.entity.AgentProviderEntity;
import org.acme.panache.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public record UserRecord(String id, String name, List<AgentProviderEntity> agents)
{

    private UserRecord(Builder builder) {
        this(builder.id, builder.name, builder.agents);
    }

    public  UserRecord from(UserEntity userEntity) {
        return new UserRecord(userEntity.getId(),userEntity.getName(),userEntity.getAgentProviders());
    }

    public static class Builder
    {

        private String id;

        private String name;

        private List<AgentProviderEntity> agents = new ArrayList<>();

        public Builder() {
            //Only Use public
        }

        public Builder(UserEntity userEntity) {
            this.id     = userEntity.getId();
            this.name   = userEntity.getName();
            this.agents = userEntity.getAgentProviders();

        }

        public Builder setAgent(AgentProviderEntity agentProvider) {
            agents.add(agentProvider);
            return this;
        }

        public Builder setAgent(List<AgentProviderEntity> agentProvider) {
            agents.addAll(agentProvider);
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public UserRecord build() {
            return new UserRecord(this);
        }




    }

    @Override
    public String toString() {
        return "UserRecord{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", agents=" + agents + '}';
    }

    public static void main(String[] args) {

        AgentProviderEntity agentProviderEntity = new AgentProviderEntity();
        agentProviderEntity.setName("SFSD");

        UserRecord record = new UserRecord.Builder().setName("F").setId("FF").setAgent(agentProviderEntity).build();
        System.out.println(record);
    }




}
