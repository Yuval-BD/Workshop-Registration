package patmal.workshops.logic.impl;

import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.model.impl.InstructorImpl;
import patmal.workshops.model.impl.StudentImpl;
import patmal.workshops.model.impl.TeamImpl;
import patmal.workshops.model.impl.WorkshopImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkshopSystemImpl implements WorkshopSystem {
    private final Map<String, InstructorImpl> instructors = new LinkedHashMap<>();
    private final Map<String, StudentImpl> students = new LinkedHashMap<>();
    private final Map<Integer, WorkshopImpl> workshops = new LinkedHashMap<>();
    private final Map<String, TeamImpl> teams = new LinkedHashMap<>();

    private int workshopId = 1;
    private int teamId = 1;

    @Override
    public Instructor createInstructor(String id, String name, String email) {
        if(instructors.containsKey(id)) {
            throw new IllegalArgumentException("Instructor with id " + id + " already exists");
        }

        InstructorImpl instructor = new InstructorImpl(id, name, email);
        instructors.put(id, instructor);
        return instructor;
    }

    @Override
    public Student createStudent(String id, String name, String email) {
        if(students.containsKey(id)) {
            throw new IllegalArgumentException("Student with id " + id + " already exists");
        }

        StudentImpl student = new StudentImpl(id, name, email);
        students.put(id, student);
        return student;
    }

    @Override
    public Workshop createWorkshop(String name, String description) {
        WorkshopImpl workshop = new WorkshopImpl(workshopId, name, description);
        workshops.put(workshop.getId(), workshop);
        workshopId++;
        return workshop;
    }

    @Override
    public Team createTeam(String name, Workshop workshop, List<String> members) {
        if (workshop == null || members == null) {
            throw new IllegalArgumentException("Workshop and members are required");
        }

        WorkshopImpl workshopImpl = workshops.get(workshop.getId());
        if (workshopImpl == null) {
            throw new IllegalArgumentException("Workshop with id " + workshop.getId() + " does not exist");
        }

        Map<String, StudentImpl> possibleMembers = new LinkedHashMap<>();
        for (String member : members) {
            if (possibleMembers.containsKey(member)) {
                throw new IllegalArgumentException("Members list contains duplicate id " + member);
            }

            StudentImpl s = students.get(member);
            if (s == null) {
                throw new IllegalArgumentException("Student with id " + member + " does not exist");
            }

            String teamName = s.getTeamName();
            if (teamName != null) {
                throw new IllegalArgumentException("Student " + member + " is already a member of team " + teamName);
            }

            possibleMembers.put(member, s);
        }

        TeamImpl team = new TeamImpl(teamId++, name, workshopImpl);
        for (StudentImpl s : possibleMembers.values()) {
            team.addStudent(s);
        }
        teams.put(team.getId().toString(), team);
        return team;
    }

    @Override
    public Student getStudent(String s) {
        return students.get(s);
    }


}

