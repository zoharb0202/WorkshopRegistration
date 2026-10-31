package patmal.workshops.model;

import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;

import java.util.ArrayList;
import java.util.List;

public class StudentImpl extends PersonImpl implements Student
{
    private Team team;

    public StudentImpl(String id, String name, String email)
    {
        super(id, name, email);
    }

    @Override
    public String getTeamName()
    {
        if (team == null)
        {
            return null;
        }
        return team.getName();
    }

    @Override
    public Instructor getInstructor()
    {
        if (team == null || team.getWorkshop() == null)
        {
            return null;
        }
        return team.getWorkshop().getInstructor();
    }

    @Override
    public List<Student> getTeamMates()
    {
        List<Student> teamMates = new ArrayList<Student>();
        if (team == null)
        {
            return teamMates;
        }

        for (Student student : team.getStudents())
        {
            if (student != this)
            {
                teamMates.add(student);
            }
        }
        return teamMates;
    }

    public Team getTeam()
    {
        return team;
    }

    public boolean isFree()
    {
        return team == null;
    }

    public void setTeam(Team team)
    {
        this.team = team;
    }
}
