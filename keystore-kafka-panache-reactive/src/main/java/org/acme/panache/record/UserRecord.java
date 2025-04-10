package org.acme.panache.record;

import org.acme.panache.entity.AgentProviderEntity;
import org.acme.panache.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

//public record UserRecord(Name name, Id id)
public record UserRecord(String id, String name)
{

    public static UserRecord from(UserEntity entity){
        return new UserRecord(entity.getId(), entity.getName());
    }



}
