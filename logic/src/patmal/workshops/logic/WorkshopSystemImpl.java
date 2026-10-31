package patmal.workshops.logic;

import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.model.InstructorImpl;
import patmal.workshops.model.StudentImpl;
import patmal.workshops.model.TeamImpl;
import patmal.workshops.model.WorkshopImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkshopSystemImpl implements WorkshopSystem
{
    private final Map<String, InstructorImpl> instructors = new LinkedHashMap<String, InstructorImpl>();
    private final Map<String, StudentImpl> students = new LinkedHashMap<String, StudentImpl>();
    private final Map<Integer, WorkshopImpl> workshops = new LinkedHashMap<Integer, WorkshopImpl>();
    private final Map<Integer, TeamImpl> teams = new LinkedHashMap<Integer, TeamImpl>();

    private int nextWorkshopId = 1;
    private int nextTeamId = 1;

    public WorkshopSystemImpl()
    {
    }

    @Override
    public Instructor createInstructor(String id, String name, String email)
    {
        checkId(id, "instructor");
        if (instructors.containsKey(id))
        {
            throw new IllegalArgumentException("An instructor with the id '" + id + "' already exists");
        }

        InstructorImpl instructor = new InstructorImpl(id, name, email);
        instructors.put(id, instructor);
        return instructor;
    }

    @Override
    public Workshop createWorkshop(String name, String description)
    {
        WorkshopImpl workshop = new WorkshopImpl(nextWorkshopId, name, description);
        workshops.put(workshop.getId(), workshop);
        nextWorkshopId++;
        return workshop;
    }

    @Override
    public void associateWorkshopWithInstructor(Instructor instructor, Workshop workshop)
    {
        if (instructor == null || !instructors.containsKey(instructor.getId()))
        {
            throw new IllegalArgumentException("The instructor is not registered in the system");
        }
        if (workshop == null || !workshops.containsKey(workshop.getId()))
        {
            throw new IllegalArgumentException("The workshop is not registered in the system");
        }

        InstructorImpl newInstructor = instructors.get(instructor.getId());
        WorkshopImpl theWorkshop = workshops.get(workshop.getId());

        InstructorImpl oldInstructor = (InstructorImpl) theWorkshop.getInstructor();
        if (oldInstructor != null && oldInstructor != newInstructor)
        {
            oldInstructor.removeWorkshop(theWorkshop);
        }

        theWorkshop.setInstructor(newInstructor);
        newInstructor.addWorkshop(theWorkshop);
    }

    @Override
    public Student createStudent(String id, String name, String email)
    {
        checkId(id, "student");
        if (students.containsKey(id))
        {
            throw new IllegalArgumentException("A student with the id '" + id + "' already exists");
        }

        StudentImpl student = new StudentImpl(id, name, email);
        students.put(id, student);
        return student;
    }

    @Override
    public Team createTeam(String name, Workshop workshop, List<String> members)
    {
        if (workshop == null || !workshops.containsKey(workshop.getId()))
        {
            throw new IllegalArgumentException("The workshop of the team is not registered in the system");
        }
        if (members == null || members.isEmpty())
        {
            throw new IllegalArgumentException("A team must have at least one student");
        }

        List<Student> teamStudents = new ArrayList<Student>();
        for (String studentId : members)
        {
            StudentImpl student = students.get(studentId);
            if (student == null)
            {
                throw new IllegalArgumentException("There is no student with the id '" + studentId + "'");
            }
            if (teamStudents.contains(student))
            {
                throw new IllegalArgumentException("The student '" + studentId + "' appears more than once in the team");
            }
            if (!student.isFree())
            {
                throw new IllegalArgumentException("The student '" + studentId + "' is already in the team " + student.getTeamName());
            }
            teamStudents.add(student);
        }

        TeamImpl team = new TeamImpl(nextTeamId, name, workshops.get(workshop.getId()), teamStudents);
        for (Student student : teamStudents)
        {
            ((StudentImpl) student).setTeam(team);
        }
        teams.put(team.getId(), team);
        nextTeamId++;
        return team;
    }

    @Override
    public List<Team> getTeams()
    {
        return new ArrayList<Team>(teams.values());
    }

    // an unknown id is not an error: getX(id) returns null and getStudentsOfX(id) returns an empty list
    @Override
    public Team getTeam(Integer id)
    {
        return teams.get(id);
    }

    @Override
    public List<Student> getStudents()
    {
        return new ArrayList<Student>(students.values());
    }

    @Override
    public Student getStudent(String id)
    {
        return students.get(id);
    }

    @Override
    public List<Workshop> getWorkshops()
    {
        return new ArrayList<Workshop>(workshops.values());
    }

    @Override
    public Workshop getWorkshop(Integer id)
    {
        return workshops.get(id);
    }

    @Override
    public List<Instructor> getInstructors()
    {
        return new ArrayList<Instructor>(instructors.values());
    }

    @Override
    public Instructor getInstructor(String id)
    {
        return instructors.get(id);
    }

    @Override
    public List<Student> getFreeStudents()
    {
        List<Student> freeStudents = new ArrayList<Student>();
        for (StudentImpl student : students.values())
        {
            if (student.isFree())
            {
                freeStudents.add(student);
            }
        }
        return freeStudents;
    }

    @Override
    public List<Student> getStudentsOfInstructor(String instructorId)
    {
        List<Student> result = new ArrayList<Student>();
        InstructorImpl instructor = instructors.get(instructorId);
        if (instructor == null)
        {
            return result;
        }

        for (Workshop workshop : instructor.getWorkshops())
        {
            for (Student student : getStudentsOfWorkshop(workshop.getId()))
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
        List<Student> result = new ArrayList<Student>();
        WorkshopImpl workshop = workshops.get(workshopId);
        if (workshop == null)
        {
            return result;
        }

        for (TeamImpl team : teams.values())
        {
            if (team.getWorkshop() == workshop)
            {
                result.addAll(team.getStudents());
            }
        }
        return result;
    }

    private void checkId(String id, String kind)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException("The id of a new " + kind + " cannot be empty");
        }
    }
}
