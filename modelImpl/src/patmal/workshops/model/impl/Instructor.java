package patmal.workshops.model.impl;

import patmal.workshops.api.model.Workshop;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends Person implements patmal.workshops.api.model.Instructor {
    private final List<Workshop> workshops = new ArrayList<>();

    public Instructor(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public List<Workshop> getWorkshops() {
        return workshops;
    }
}
