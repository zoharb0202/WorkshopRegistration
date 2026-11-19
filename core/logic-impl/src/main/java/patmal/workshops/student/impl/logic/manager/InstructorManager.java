package patmal.workshops.student.impl.logic.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.student.impl.model.InstructorImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InstructorManager
{
    private static final Logger logger = LogManager.getLogger(InstructorManager.class);

    private final Map<String, InstructorImpl> instructors = new LinkedHashMap<String, InstructorImpl>();

    public InstructorImpl createInstructor(String id, String name, String email)
    {
        InputValidator.checkId(id, "instructor");
        InputValidator.checkEmail(email, "instructor");
        if (instructors.containsKey(id))
        {
            throw reject("An instructor with the id '" + id + "' already exists");
        }

        InstructorImpl instructor = new InstructorImpl(id, name, email);
        instructors.put(id, instructor);
        logger.info("Instructor created: {}", instructor);
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
            throw reject("The instructor is not registered in the system");
        }
        return instructors.get(instructor.getId());
    }

    public List<Instructor> getInstructors()
    {
        return new ArrayList<Instructor>(instructors.values());
    }

    private IllegalArgumentException reject(String message)
    {
        logger.warn("Rejected: {}", message);
        return new IllegalArgumentException(message);
    }
}
