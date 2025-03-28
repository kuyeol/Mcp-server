package org.acme.panache;

import org.acme.panache.record.UserRecord;

public class TestMain
{

    public static void main(String[] args) {
        UserRecord record = new UserRecord.Builder().setName("F").setId("FF").build();
    }

}
