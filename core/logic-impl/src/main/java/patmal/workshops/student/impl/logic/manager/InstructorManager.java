package patmal.workshops.student.impl.logic.manager;

import patmal.workshops.api.model.Instructor;
import patmal.workshops.student.impl.model.InstructorImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InstructorManager
{
    private final Map<String, InstructorImpl> instructors = new LinkedHashMap<String, InstructorImpl>();

    public InstructorImpl createInstructor(String id, String name, String email)
    {
        InputValidator.checkId(id, "instructor");
        if (instructors.containsKey(id))
        {
            throw new IllegalArgumentException("An instructor with the id '" + id + "' already exists");
        }

        InstructorImpl instructor = new InstructorImpl(id, name, email);
        instructors.put(id, instructor);
        return instructor;
    }

    public InstructorImpl getInstructor(String id)
    {
        return instructors.get(id);
    }

    public InstructorImpl getRegisteredInstructor(Instructor instructor)
    {
        if (instructor == null || !instructors.containsKey(instructor.getId()))
        {
            throw new IllegalArgumentException("The instructor is not registered in the system");
        }
        return instructors.get(instructor.getId());
    }

    public List<Instructor> getInstructors()
    {
        return new ArrayList<Instructor>(instructors.values());
    }
}
