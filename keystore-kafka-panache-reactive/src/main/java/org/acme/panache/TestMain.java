package org.acme.panache;

import org.acme.panache.record.Id;
import org.acme.panache.record.Name;
import org.acme.panache.record.TestRecord;

public class TestMain
{

    public static void main(String[] args) {
        
        String my ="asdf";
        Name       name       = new Name(my);
        Id         id         = new Id("id9090");

        TestRecord buildtest = new TestRecord.Builder().id(id).name(name).build();

        TestRecord testRecord = new TestRecord(name, id);
        TestRecord test       = testRecord.from(testRecord);
        TestRecord test2      = new TestRecord(name, id).from(testRecord);

        TestRecord.testRe.from(testRecord);

        System.out.println(testRecord);
        System.out.println(test);
        System.out.println(test2);
        System.out.println(buildtest);

    }




}
