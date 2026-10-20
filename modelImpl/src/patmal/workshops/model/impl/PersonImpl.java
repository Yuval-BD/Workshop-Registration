package patmal.workshops.model.impl;

import patmal.workshops.api.model.Person;

public abstract class PersonImpl implements Person {
    private final String id;
    private final String name;
    private final String email;

    protected  PersonImpl(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Person p && id.equals(p.getId());
    }

    @Override
    public int hashCode() { return id.hashCode(); }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
