package patmal.workshops.model.impl;

import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Workshop;

import java.util.ArrayList;
import java.util.List;

public class InstructorImpl extends PersonImpl implements Instructor {
    private final List<Workshop> workshops = new ArrayList<>();

    public InstructorImpl(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public List<Workshop> getWorkshops() {
        return new ArrayList<>(workshops);
    }

    public void addWorkshop(WorkshopImpl workshop) {
        if (!workshops.contains(workshop)) {
            workshops.add(workshop);
            workshop.setInstructor(this);
        }
    }

    public void removeWorkshop(WorkshopImpl workshop) {
        if (workshops.remove(workshop)) {
            workshop.setInstructor(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof InstructorImpl i && this.getId().equals(i.getId());
    }

    @Override
    public int hashCode() { return this.getId().hashCode(); }
}
