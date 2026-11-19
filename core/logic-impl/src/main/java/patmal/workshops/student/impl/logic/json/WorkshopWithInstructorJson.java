package patmal.workshops.student.impl.logic.json;

public class WorkshopWithInstructorJson
{
    private final Integer id;
    private final String name;
    private final String description;
    private final PersonJson instructor;

    public WorkshopWithInstructorJson(Integer id, String name, String description, PersonJson instructor)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.instructor = instructor;
    }
}
