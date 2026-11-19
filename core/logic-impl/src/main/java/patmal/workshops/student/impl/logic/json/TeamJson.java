package patmal.workshops.student.impl.logic.json;

import java.util.List;

public class TeamJson
{
    private final Integer id;
    private final String name;
    private final List<PersonJson> students;
    private final WorkshopWithInstructorJson workshop;

    public TeamJson(Integer id, String name, List<PersonJson> students, WorkshopWithInstructorJson workshop)
    {
        this.id = id;
        this.name = name;
        this.students = students;
        this.workshop = workshop;
    }
}
