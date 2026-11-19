package patmal.workshops.student.impl.logic.json;

import java.util.List;

public class TeamWithStudentsJson
{
    private final Integer id;
    private final String name;
    private final List<PersonJson> students;

    public TeamWithStudentsJson(Integer id, String name, List<PersonJson> students)
    {
        this.id = id;
        this.name = name;
        this.students = students;
    }
}
