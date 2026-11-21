package patmal.workshops.student.impl.app;

import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.student.impl.logic.WorkshopSystemImpl;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        WorkshopSystem system = new WorkshopSystemImpl();

        // Instructors
        system.createInstructor("i1", "Dana Levi", "dana.levi@univ.edu");
        system.createInstructor("i2", "Yossi Mizrahi", "yossi.mizrahi@univ.edu");
        system.createInstructor("i3", "Noa Katz", "noa.katz@univ.edu");

        // Workshops (ids 1..4 assigned by the system)
        Workshop java   = system.createWorkshop("Java Workshop", "Introduction to Java");
        Workshop web    = system.createWorkshop("Web Development", "HTML, CSS and JavaScript");
        Workshop db     = system.createWorkshop("Databases", "Relational databases and SQL");
        Workshop mobile = system.createWorkshop("Mobile Apps", "Android fundamentals");

        // i1 teaches two workshops, i2 one, i3 one
        system.associateWorkshopWithInstructor(system.getInstructor("i1"), java);
        system.associateWorkshopWithInstructor(system.getInstructor("i1"), web);
        system.associateWorkshopWithInstructor(system.getInstructor("i2"), db);
        system.associateWorkshopWithInstructor(system.getInstructor("i3"), mobile);

        // Students
        system.createStudent("s1", "Alice Cohen",  "alice@students.edu");
        system.createStudent("s2", "Ben Shapiro",  "ben@students.edu");
        system.createStudent("s3", "Carmel Bar",   "carmel@students.edu");
        system.createStudent("s4", "David Peretz", "david@students.edu");
        system.createStudent("s5", "Ella Ron",     "ella@students.edu");
        system.createStudent("s6", "Noam Tal",     "noam@students.edu");
        system.createStudent("s7", "Omer Gal",     "omer@students.edu");
        system.createStudent("s8", "Yael Mor",     "yael@students.edu");

        // Teams (s7 and s8 stay free)
        system.createTeam("Alpha", java, List.of("s1"));
        system.createTeam("Beta",  web,  List.of("s2", "s3"));
        system.createTeam("Gamma", db,   List.of("s4", "s5", "s6"));

        // Export to the current folder
        system.exportInstructorsDataToJson(new File("instructors.json"));
        system.exportStudentsDataToJson(new File("students.json"));
        system.exportTeamsDataToJson(new File("teams.json"));

        System.out.println("Created 3 instructors, 4 workshops, 8 students, 3 teams (s7, s8 free).");
        System.out.println("Wrote instructors.json, students.json, teams.json to "
                + new File(".").getAbsolutePath());
    }
}