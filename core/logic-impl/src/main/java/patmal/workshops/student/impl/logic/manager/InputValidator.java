package patmal.workshops.student.impl.logic.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputValidator
{
    private static final Logger logger = LogManager.getLogger(InputValidator.class);

    private InputValidator()
    {
    }

    public static void checkId(String id, String kind)
    {
        if (id == null || id.trim().isEmpty())
        {
            String message = "The id of a new " + kind + " cannot be empty";
            logger.warn("Rejected: {}", message);
            throw new IllegalArgumentException(message);
        }
    }
}
