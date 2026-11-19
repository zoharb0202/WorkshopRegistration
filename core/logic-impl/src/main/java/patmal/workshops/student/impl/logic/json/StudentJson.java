package patmal.workshops.student.impl.logic.json;

public class StudentJson
{
    private final String id;
    private final String name;
    private final String email;
    private final StudentTeamJson team;

    public StudentJson(String id, String name, String email, StudentTeamJson team)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.team = team;
    }
}
