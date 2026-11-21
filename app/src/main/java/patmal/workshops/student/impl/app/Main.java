package patmal.workshops.student.impl.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.student.impl.logic.WorkshopSystemImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class Main
{
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String DEFAULT_STUDENTS_FILE = "students.csv";

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

        File studentsFile = getStudentsFile(args);
        List<Student> students = system.importStudentDataFromFile(studentsFile);
        logger.info("{} students were imported from {}", students.size(), studentsFile);

        system.createTeam("Alpha", java, Arrays.asList("s1", "s2"));
        system.createTeam("Beta", web, Arrays.asList("s3"));
        system.createTeam("Gamma", databases, Arrays.asList("s4", "s5"));

        logger.info("Demo data is ready: {} instructors, {} workshops, {} students ({} free), {} teams",
                system.getInstructors().size(), system.getWorkshops().size(), system.getStudents().size(),
                system.getFreeStudents().size(), system.getTeams().size());

        File instructorsFile = new File("instructors.json");
        File studentsJsonFile = new File("students.json");
        File teamsFile = new File("teams.json");

        system.exportInstructorsDataToJson(instructorsFile);
        system.exportStudentsDataToJson(studentsJsonFile);
        system.exportTeamsDataToJson(teamsFile);

        logger.info("The files were written to {}", instructorsFile.getAbsoluteFile().getParent());
        logger.info("workshop-cli finished");
    }

    private static File getStudentsFile(String[] args)
    {
        if (args.length > 0)
        {
            return new File(args[0]);
        }

        logger.info("No students file was given - using the {} inside the application", DEFAULT_STUDENTS_FILE);
        try (InputStream in = Main.class.getResourceAsStream("/" + DEFAULT_STUDENTS_FILE))
        {
            if (in == null)
            {
                throw new IllegalStateException(DEFAULT_STUDENTS_FILE + " was not found inside the application");
            }
            Path tempFile = Files.createTempFile("students", ".csv");
            tempFile.toFile().deleteOnExit();
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return tempFile.toFile();
        }
        catch (IOException e)
        {
            throw new UncheckedIOException("Could not read " + DEFAULT_STUDENTS_FILE + " from inside the application", e);
        }
    }
}
