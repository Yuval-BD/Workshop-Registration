package patmal.workshops.model.impl;

import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Team;

import java.util.ArrayList;
import java.util.List;

public class    StudentImpl extends PersonImpl implements Student {
    private Team team;

    public StudentImpl(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getTeamName() {
        return team == null ? null : team.getName();
    }

    @Override
    public Instructor getInstructor() {
        return team == null ? null : team.getWorkshop().getInstructor();
    }

    @Override
    public List<Student> getTeamMates() {
        List<Student> teamMates = new ArrayList<>();
        if(team != null) {
            for(Student teammate : team.getStudents()) {
                if (!teammate.equals(this)) {
                    teamMates.add(teammate);
                }
            }
        }

        return teamMates;
    }

    void setTeam(Team team) { this.team = team; }
}
