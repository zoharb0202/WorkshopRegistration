package patmal.workshops.student.impl.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.student.impl.logic.WorkshopSystemImpl;

import java.io.File;
import java.util.Arrays;

public class Main
{
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args)
    {
        logger.info("workshop-cli started");
        WorkshopSystem system = new WorkshopSystemImpl();

        Instructor dana = system.createInstructor("i1", "Dana Levi", "dana.levi@univ.edu");
        Instructor yossi = system.createInstructor("i2", "Yossi Mizrahi", "yossi.mizrahi@univ.edu");

        Workshop java = system.createWorkshop("Java Workshop", "Object oriented programming with Java");
        Workshop web = system.createWorkshop("Web Development", "Building web applications");
        Workshop databases = system.createWorkshop("Databases", "Relational databases and SQL");

        system.associateWorkshopWithInstructor(dana, java);
        system.associateWorkshopWithInstructor(dana, web);
        system.associateWorkshopWithInstructor(yossi, databases);

        system.createStudent("s1", "Alice Cohen", "alice@students.edu");
        system.createStudent("s2", "Bob Friedman", "bob@students.edu");
        system.createStudent("s3", "Carol Dayan", "carol@students.edu");
        system.createStudent("s4", "David Peretz", "david@students.edu");
        system.createStudent("s5", "Eve Shapiro", "eve@students.edu");
        system.createStudent("s6", "Frank Golan", "frank@students.edu");

        system.createTeam("Alpha", java, Arrays.asList("s1", "s2"));
        system.createTeam("Beta", web, Arrays.asList("s3"));
        system.createTeam("Gamma", databases, Arrays.asList("s4", "s5"));

        logger.info("Demo data is ready: {} instructors, {} workshops, {} students ({} free), {} teams",
                system.getInstructors().size(), system.getWorkshops().size(), system.getStudents().size(),
                system.getFreeStudents().size(), system.getTeams().size());

        File instructorsFile = new File("instructors.json");
        File studentsFile = new File("students.json");
        File teamsFile = new File("teams.json");

        system.exportInstructorsDataToJson(instructorsFile);
        system.exportStudentsDataToJson(studentsFile);
        system.exportTeamsDataToJson(teamsFile);

        logger.info("The files were written to {}", instructorsFile.getAbsoluteFile().getParent());
        logger.info("workshop-cli finished");
    }
}
