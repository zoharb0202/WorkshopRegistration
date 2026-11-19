package patmal.workshops.student.impl.logic.json;

import java.util.List;

public class WorkshopWithTeamsJson
{
    private final Integer id;
    private final String name;
    private final String description;
    private final List<TeamWithStudentsJson> teams;

    public WorkshopWithTeamsJson(Integer id, String name, String description, List<TeamWithStudentsJson> teams)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.teams = teams;
    }
}
