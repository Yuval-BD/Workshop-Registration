package patmal.workshops.logic.impl;

import patmal.workshops.api.logic.WorkshopSystem;

import java.util.List;

public class WorkshopSystemImpl implements WorkshopSystem {

    @Override
    public patmal.workshops.api.model.Instructor createInstructor(String s, String s1, String s2) {
        return null;
    }

    @Override
    public patmal.workshops.api.model.Workshop createWorkshop(String s, String s1) {
        return null;
    }

    @Override
    public void associateWorkshopWithInstructor(patmal.workshops.api.model.Instructor instructor, patmal.workshops.api.model.Workshop workshop) {

    }

    @Override
    public patmal.workshops.api.model.Student createStudent(String s, String s1, String s2) {
        return null;
    }

    @Override
    public patmal.workshops.api.model.Team createTeam(String s, patmal.workshops.api.model.Workshop workshop, List<String> list) {
        return null;
    }

    @Override
    public List<patmal.workshops.api.model.Team> getTeams() {
        return List.of();
    }

    @Override
    public patmal.workshops.api.model.Team getTeam(Integer integer) {
        return null;
    }

    @Override
    public List<patmal.workshops.api.model.Student> getStudents() {
        return List.of();
    }

    @Override
    public patmal.workshops.api.model.Student getStudent(String s) {
        return null;
    }

    @Override
    public List<patmal.workshops.api.model.Workshop> getWorkshops() {
        return List.of();
    }

    @Override
    public patmal.workshops.api.model.Workshop getWorkshop(Integer integer) {
        return null;
    }

    @Override
    public List<patmal.workshops.api.model.Instructor> getInstructors() {
        return List.of();
    }

    @Override
    public patmal.workshops.api.model.Instructor getInstructor(String s) {
        return null;
    }

    @Override
    public List<patmal.workshops.api.model.Student> getFreeStudents() {
        return List.of();
    }

    @Override
    public List<patmal.workshops.api.model.Student> getStudentsOfInstructor(String s) {
        return List.of();
    }

    @Override
    public List<patmal.workshops.api.model.Student> getStudentsOfWorkshop(Integer integer) {
        return List.of();
    }
}
