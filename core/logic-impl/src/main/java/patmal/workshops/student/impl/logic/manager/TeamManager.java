package patmal.workshops.student.impl.logic.manager;

import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.student.impl.model.StudentImpl;
import patmal.workshops.student.impl.model.TeamImpl;
import patmal.workshops.student.impl.model.WorkshopImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TeamManager
{
    private final Map<Integer, TeamImpl> teams = new LinkedHashMap<Integer, TeamImpl>();
    private final StudentManager studentManager;
    private int nextTeamId = 1;

    public TeamManager(StudentManager studentManager)
    {
        this.studentManager = studentManager;
    }

    public TeamImpl createTeam(String name, WorkshopImpl workshop, List<String> members)
    {
        if (members == null || members.isEmpty())
        {
            throw new IllegalArgumentException("A team must have at least one student");
        }

        List<Student> teamStudents = new ArrayList<Student>();
        for (String studentId : members)
        {
            StudentImpl student = studentManager.getStudent(studentId);
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

        TeamImpl team = new TeamImpl(nextTeamId, name, workshop, teamStudents);
        for (Student student : teamStudents)
        {
            ((StudentImpl) student).setTeam(team);
        }
        teams.put(team.getId(), team);
        nextTeamId++;
        return team;
    }

    public TeamImpl getTeam(Integer id)
    {
        return teams.get(id);
    }

    public List<Team> getTeams()
    {
        return new ArrayList<Team>(teams.values());
    }

    public List<Student> getStudentsOfWorkshop(WorkshopImpl workshop)
    {
        List<Student> result = new ArrayList<Student>();
        for (TeamImpl team : teams.values())
        {
            if (team.getWorkshop() == workshop)
            {
                result.addAll(team.getStudents());
            }
        }
        return result;
    }
}
