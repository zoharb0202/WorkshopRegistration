package patmal.workshops.student.impl.logic;

import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.student.impl.logic.manager.InstructorManager;
import patmal.workshops.student.impl.logic.manager.StudentManager;
import patmal.workshops.student.impl.logic.manager.TeamManager;
import patmal.workshops.student.impl.logic.manager.WorkshopManager;
import patmal.workshops.student.impl.model.InstructorImpl;
import patmal.workshops.student.impl.model.WorkshopImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorkshopSystemImpl implements WorkshopSystem
{
    private final InstructorManager instructorManager = new InstructorManager();
    private final StudentManager studentManager = new StudentManager();
    private final WorkshopManager workshopManager = new WorkshopManager();
    private final TeamManager teamManager = new TeamManager(studentManager);

    public WorkshopSystemImpl()
    {
    }

    @Override
    public Instructor createInstructor(String id, String name, String email)
    {
        return instructorManager.createInstructor(id, name, email);
    }

    @Override
    public Workshop createWorkshop(String name, String description)
    {
        return workshopManager.createWorkshop(name, description);
    }

    @Override
    public void associateWorkshopWithInstructor(Instructor instructor, Workshop workshop)
    {
        InstructorImpl theInstructor = instructorManager.getRegisteredInstructor(instructor);
        WorkshopImpl theWorkshop = workshopManager.getRegisteredWorkshop(workshop, "The workshop is not registered in the system");
        workshopManager.associateWithInstructor(theWorkshop, theInstructor);
    }

    @Override
    public Student createStudent(String id, String name, String email)
    {
        return studentManager.createStudent(id, name, email);
    }

    @Override
    public Team createTeam(String name, Workshop workshop, List<String> members)
    {
        WorkshopImpl theWorkshop = workshopManager.getRegisteredWorkshop(workshop, "The workshop of the team is not registered in the system");
        return teamManager.createTeam(name, theWorkshop, members);
    }

    @Override
    public List<Team> getTeams()
    {
        return teamManager.getTeams();
    }

    // an unknown id is not an error: getX(id) returns null and getStudentsOfX(id) returns an empty list
    @Override
    public Team getTeam(Integer id)
    {
        return teamManager.getTeam(id);
    }

    @Override
    public List<Student> getStudents()
    {
        return studentManager.getStudents();
    }

    @Override
    public Student getStudent(String id)
    {
        return studentManager.getStudent(id);
    }

    @Override
    public List<Workshop> getWorkshops()
    {
        return workshopManager.getWorkshops();
    }

    @Override
    public Workshop getWorkshop(Integer id)
    {
        return workshopManager.getWorkshop(id);
    }

    @Override
    public List<Instructor> getInstructors()
    {
        return instructorManager.getInstructors();
    }

    @Override
    public Instructor getInstructor(String id)
    {
        return instructorManager.getInstructor(id);
    }

    @Override
    public List<Student> getFreeStudents()
    {
        return studentManager.getFreeStudents();
    }

    @Override
    public List<Student> getStudentsOfInstructor(String instructorId)
    {
        List<Student> result = new ArrayList<Student>();
        InstructorImpl instructor = instructorManager.getInstructor(instructorId);
        if (instructor == null)
        {
            return result;
        }

        for (Workshop workshop : instructor.getWorkshops())
        {
            for (Student student : teamManager.getStudentsOfWorkshop((WorkshopImpl) workshop))
            {
                if (!result.contains(student))
                {
                    result.add(student);
                }
            }
        }
        return result;
    }

    @Override
    public List<Student> getStudentsOfWorkshop(Integer workshopId)
    {
        WorkshopImpl workshop = workshopManager.getWorkshop(workshopId);
        if (workshop == null)
        {
            return new ArrayList<Student>();
        }
        return teamManager.getStudentsOfWorkshop(workshop);
    }

    @Override
    public void exportInstructorsDataToJson(File targetFileName)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void exportStudentsDataToJson(File targetFileName)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void exportTeamsDataToJson(File targetFileName)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
