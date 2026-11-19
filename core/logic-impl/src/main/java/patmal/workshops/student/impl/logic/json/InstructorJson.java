package patmal.workshops.student.impl.logic.json;

import java.util.List;

public class InstructorJson
{
    private final String id;
    private final String name;
    private final String email;
    private final List<WorkshopWithTeamsJson> workshops;

    public InstructorJson(String id, String name, String email, List<WorkshopWithTeamsJson> workshops)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.workshops = workshops;
    }
}
