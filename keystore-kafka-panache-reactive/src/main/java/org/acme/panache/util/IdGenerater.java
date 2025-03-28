package org.acme.panache.util;

import java.util.UUID;

public final class IdGenerater
{

    public static String create() {
        String id = UUID.randomUUID().toString();
        return id;
    }




}
