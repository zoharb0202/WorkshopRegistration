package patmal.workshops.student.impl.logic.json;

import java.util.List;

public class StudentTeamJson
{
    private final Integer id;
    private final String name;
    private final List<String> studentIds;
    private final WorkshopWithInstructorJson workshop;

    public StudentTeamJson(Integer id, String name, List<String> studentIds, WorkshopWithInstructorJson workshop)
    {
        this.id = id;
        this.name = name;
        this.studentIds = studentIds;
        this.workshop = workshop;
    }
}
