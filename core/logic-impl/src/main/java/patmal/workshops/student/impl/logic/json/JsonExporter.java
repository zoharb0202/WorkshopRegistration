package patmal.workshops.student.impl.logic.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Person;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.student.impl.model.StudentImpl;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class JsonExporter
{
    private static final Logger logger = LogManager.getLogger(JsonExporter.class);

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public void exportInstructors(List<Instructor> instructors, List<Team> allTeams, File targetFile)
    {
        List<InstructorJson> result = new ArrayList<InstructorJson>();
        for (Instructor instructor : instructors)
        {
            List<WorkshopWithTeamsJson> workshops = new ArrayList<WorkshopWithTeamsJson>();
            for (Workshop workshop : instructor.getWorkshops())
            {
                List<TeamWithStudentsJson> teams = new ArrayList<TeamWithStudentsJson>();
                for (Team team : allTeams)
                {
                    if (team.getWorkshop() == workshop)
                    {
                        teams.add(new TeamWithStudentsJson(team.getId(), team.getName(), toPersonList(team.getStudents())));
                    }
                }
                workshops.add(new WorkshopWithTeamsJson(workshop.getId(), workshop.getName(), workshop.getDescription(), teams));
            }
            result.add(new InstructorJson(instructor.getId(), instructor.getName(), instructor.getEmail(), workshops));
        }

        writeToFile(result, targetFile);
        logger.info("Exported {} instructors to {}", result.size(), targetFile);
    }

    public void exportStudents(List<Student> students, File targetFile)
    {
        List<StudentJson> result = new ArrayList<StudentJson>();
        for (Student student : students)
        {
            StudentTeamJson teamJson = null;
            Team team = ((StudentImpl) student).getTeam();
            if (team != null)
            {
                List<String> studentIds = new ArrayList<String>();
                for (Student member : team.getStudents())
                {
                    studentIds.add(member.getId());
                }
                teamJson = new StudentTeamJson(team.getId(), team.getName(), studentIds, toWorkshopWithInstructor(team.getWorkshop()));
            }
            result.add(new StudentJson(student.getId(), student.getName(), student.getEmail(), teamJson));
        }

        writeToFile(result, targetFile);
        logger.info("Exported {} students to {}", result.size(), targetFile);
    }

    public void exportTeams(List<Team> teams, File targetFile)
    {
        List<TeamJson> result = new ArrayList<TeamJson>();
        for (Team team : teams)
        {
            result.add(new TeamJson(team.getId(), team.getName(), toPersonList(team.getStudents()), toWorkshopWithInstructor(team.getWorkshop())));
        }

        writeToFile(result, targetFile);
        logger.info("Exported {} teams to {}", result.size(), targetFile);
    }

    private WorkshopWithInstructorJson toWorkshopWithInstructor(Workshop workshop)
    {
        PersonJson instructor = null;
        if (workshop.getInstructor() != null)
        {
            instructor = toPerson(workshop.getInstructor());
        }
        return new WorkshopWithInstructorJson(workshop.getId(), workshop.getName(), workshop.getDescription(), instructor);
    }

    private List<PersonJson> toPersonList(List<Student> students)
    {
        List<PersonJson> result = new ArrayList<PersonJson>();
        for (Student student : students)
        {
            result.add(toPerson(student));
        }
        return result;
    }

    private PersonJson toPerson(Person person)
    {
        return new PersonJson(person.getId(), person.getName(), person.getEmail());
    }

    private void writeToFile(Object data, File targetFile)
    {
        try
        {
            Files.writeString(targetFile.toPath(), gson.toJson(data), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            logger.warn("Could not write the file {}: {}", targetFile, e.getMessage());
            throw new UncheckedIOException("Could not write the file " + targetFile, e);
        }
    }
}
