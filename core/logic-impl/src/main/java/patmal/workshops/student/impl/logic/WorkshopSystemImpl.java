package patmal.workshops.student.impl.logic;

import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.api.model.Person;

import patmal.workshops.student.impl.model.InstructorImpl;
import patmal.workshops.student.impl.model.StudentImpl;
import patmal.workshops.student.impl.model.WorkshopImpl;
import patmal.workshops.student.impl.model.TeamImpl;

import com.sanctionco.jmail.JMail;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

public class WorkshopSystemImpl implements WorkshopSystem {
    private final Map<String, InstructorImpl> instructors = new LinkedHashMap<>();
    private final Map<String, StudentImpl> students = new LinkedHashMap<>();
    private final Map<Integer, WorkshopImpl> workshops = new LinkedHashMap<>();
    private final Map<Integer, TeamImpl> teams = new LinkedHashMap<>();

    private int workshopId = 1;
    private int teamId = 1;

    private final ObjectMapper jsonMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    //*** Creators ***//
    @Override
    public Instructor createInstructor(String id, String name, String email) {
        validateEmail(email);

        if (instructors.containsKey(id)) {
            throw new IllegalArgumentException("Instructor with id " + id + " already exists");
        }

        InstructorImpl instructor = new InstructorImpl(id, name, email);
        instructors.put(id, instructor);
        return instructor;
    }

    @Override
    public Student createStudent(String id, String name, String email) {
        validateEmail(email);

        if (students.containsKey(id)) {
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
        teams.put(team.getId(), team);
        return team;
    }

    //*** Connectors ***//
    @Override
    public void associateWorkshopWithInstructor(Instructor instructor, Workshop workshop) {
        if (instructor == null || workshop == null) {
            throw new IllegalArgumentException("Instructor and workshop are required");
        }

        InstructorImpl instructorImpl = instructors.get(instructor.getId());
        if (instructorImpl == null) {
            throw new IllegalArgumentException("Instructor with id " + instructor.getId() + " does not exist");
        }

        WorkshopImpl workshopImpl = workshops.get(workshop.getId());
        if (workshopImpl == null) {
            throw new IllegalArgumentException("Workshop with id " + workshop.getId() + " does not exist");
        }

        Instructor current = workshopImpl.getInstructor();
        if (current != null) {
            if (current.equals(instructorImpl)) {
                return;
            }
            instructors.get(current.getId()).removeWorkshop(workshopImpl);
        }
        instructorImpl.addWorkshop(workshopImpl);
    }

    //*** Queries ***//
    @Override
    public Instructor getInstructor(String id) {
        return instructors.get(id);
    }

    @Override
    public List<Instructor> getInstructors() {
        return new ArrayList<>(instructors.values());
    }

    @Override
    public Student getStudent(String id) {
        return students.get(id);
    }

    @Override
    public List<Student> getStudents() {
        return new ArrayList<>(students.values());
    }

    @Override
    public Workshop getWorkshop(Integer id) {
        return workshops.get(id);
    }

    @Override
    public List<Workshop> getWorkshops() {
        return new ArrayList<>(workshops.values());
    }

    @Override
    public Team getTeam(Integer id) {
        return teams.get(id);
    }

    @Override
    public List<Team> getTeams() {
        return new ArrayList<>(teams.values());
    }

    @Override
    public List<Student> getFreeStudents() {
        List<Student> freeStudents = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getTeamName() == null) {
                freeStudents.add(student);
            }
        }
        return freeStudents;
    }

    @Override
    public List<Student> getStudentsOfInstructor(String id) {
        List<Student> result = new ArrayList<>();
        InstructorImpl instructor = instructors.get(id);
        if (instructor != null) {
            for (Workshop workshop : instructor.getWorkshops()) {
                result.addAll(getStudentsOfWorkshop(workshop.getId()));
            }
        }
        return result;
    }

    @Override
    public List<Student> getStudentsOfWorkshop(Integer id) {
        List<Student> result = new ArrayList<>();
        for (TeamImpl team : teams.values()) {
            if (team.getWorkshop().getId().equals(id)) {
                result.addAll(team.getStudents());
            }
        }
        return result;
    }

    //*** Export to json ***//
    @Override
    public void exportInstructorsDataToJson(File file) {
        ArrayNode result = jsonMapper.createArrayNode();
        for (InstructorImpl instructor : instructors.values()) {
            result.add(instructorNodeWithWorkshops(instructor));
        }
        writeJsonToFile(file, result);
    }

    @Override
    public void exportStudentsDataToJson(File file) {
        Map<String, Team> teamOf = teamByStudentId();
        ArrayNode result = jsonMapper.createArrayNode();
        for (StudentImpl student : students.values()) {
            result.add(studentNodeWithTeam(student, teamOf.get(student.getId())));
        }
        writeJsonToFile(file, result);
    }

    @Override
    public void exportTeamsDataToJson(File file) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    //*** Import from json ***//
    @Override
    public List<Student> importStudentDataFromFile(File source) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    //*** Helpers ***//
    private void validateEmail(String email) {
        if (email == null || !JMail.isValid(email)) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }

    private void writeJsonToFile(File file, Object data) {
        try {
            jsonMapper.writeValue(file, data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private ObjectNode personDetails(Person person) {
        ObjectNode node = jsonMapper.createObjectNode();
        node.put("id", person.getId());
        node.put("name", person.getName());
        node.put("email", person.getEmail());
        return node;
    }

    private ObjectNode workshopDetails(Workshop workshop, boolean withInstructor) {
        ObjectNode node = jsonMapper.createObjectNode();
        node.put("id", workshop.getId());
        node.put("name", workshop.getName());
        node.put("description", workshop.getDescription());
        if (withInstructor) {
            Instructor instructor = workshop.getInstructor();
            if (instructor == null) {
                node.putNull("instructor");
            } else {
                node.set("instructor", personDetails(instructor));
            }
        }
        return node;
    }

    private ObjectNode instructorNodeWithWorkshops(Instructor instructor) {
        ObjectNode node = personDetails(instructor);
        ArrayNode workshopsNode = jsonMapper.createArrayNode();
        for (Workshop workshop : instructor.getWorkshops()) {
            workshopsNode.add(workshopNodeWithTeams(workshop));
        }
        node.set("workshops", workshopsNode);
        return node;
    }

    private ObjectNode workshopNodeWithTeams(Workshop workshop) {
        ObjectNode node = workshopDetails(workshop, false);
        ArrayNode teamsNode = jsonMapper.createArrayNode();
        for (TeamImpl team : teamsOfWorkshop(workshop)) {
            teamsNode.add(teamNodeWithStudents(team));
        }
        node.set("teams", teamsNode);
        return node;
    }

    private List<TeamImpl> teamsOfWorkshop(Workshop workshop) {
        List<TeamImpl> result = new ArrayList<>();
        for (TeamImpl team : teams.values()) {
            if (team.getWorkshop().getId().equals(workshop.getId())) {
                result.add(team);
            }
        }
        return result;
    }

    private ObjectNode teamNodeWithStudents(Team team) {
        ObjectNode node = jsonMapper.createObjectNode();
        node.put("id", team.getId());
        node.put("name", team.getName());
        node.set("students", studentsArray(team));
        return node;
    }

    private ArrayNode studentsArray(Team team) {
        ArrayNode arr = jsonMapper.createArrayNode();
        for (Student student : team.getStudents()) {
            arr.add(personDetails(student));
        }
        return arr;
    }

    private Map<String, Team> teamByStudentId() {
        Map<String, Team> teamOf = new HashMap<>();
        for (TeamImpl team : teams.values()) {
            for (Student student : team.getStudents()) {
                teamOf.put(student.getId(), team);
            }
        }
        return teamOf;
    }

    private ObjectNode studentNodeWithTeam(Student student, Team team) {
        ObjectNode node = personDetails(student);
        if (team == null) {
            node.putNull("team");
        } else {
            node.set("team", teamSummary(team));
        }
        return node;
    }

    private ObjectNode teamSummary(Team team) {
        ObjectNode node = jsonMapper.createObjectNode();
        node.put("id", team.getId());
        node.put("name", team.getName());
        ArrayNode ids = jsonMapper.createArrayNode();
        for (Student student : team.getStudents()) {
            ids.add(student.getId());
        }
        node.set("studentIds", ids);
        node.set("workshop", workshopDetails(team.getWorkshop(), true));
        return node;
    }
}