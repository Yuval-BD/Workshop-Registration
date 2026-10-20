package patmal.workshops.logic.impl;

import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.model.impl.InstructorImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkshopSystemImpl implements WorkshopSystem {

    private final Map<String, Instructor> instructors = new LinkedHashMap<>();

    @Override
    public Instructor createInstructor(String id, String name, String email) {
        if(instructors.containsKey(id)) {
            throw new IllegalArgumentException("Instructor with id " + id + " already exists");
        }

        Instructor instructor = new InstructorImpl(id, name, email);
        instructors.put(id, instructor);
        return instructor;
    }
}
