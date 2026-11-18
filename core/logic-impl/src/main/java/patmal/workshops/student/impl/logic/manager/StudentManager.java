package patmal.workshops.student.impl.logic.manager;

import patmal.workshops.api.model.Student;
import patmal.workshops.student.impl.model.StudentImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StudentManager
{
    private final Map<String, StudentImpl> students = new LinkedHashMap<String, StudentImpl>();

    public StudentImpl createStudent(String id, String name, String email)
    {
        InputValidator.checkId(id, "student");
        if (students.containsKey(id))
        {
            throw new IllegalArgumentException("A student with the id '" + id + "' already exists");
        }

        StudentImpl student = new StudentImpl(id, name, email);
        students.put(id, student);
        return student;
    }

    public StudentImpl getStudent(String id)
    {
        return students.get(id);
    }

    public List<Student> getStudents()
    {
        return new ArrayList<Student>(students.values());
    }

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
}
