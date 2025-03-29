package org.acme.panache.record;

public record TestRecord(Name name, Id id)
{

    static Name names;

    static Id ids;

    public static TestRecord testRe = new TestRecord(names, ids);

    public TestRecord from(TestRecord testRecord) {
        Name n1 = testRecord.name;
        Id   p1 = testRecord.id;
        return new TestRecord(n1, p1);

    }

    public static class Builder
    {

        private Name name;

        private Id   id;

        public Builder name(Name name) {
            this.name = name;
            return this;
        }

        public Builder id(Id id) {
            this.id = id;
            return this;
        }

        public TestRecord build() {
            return new TestRecord(name, id);
        }



    }




}
