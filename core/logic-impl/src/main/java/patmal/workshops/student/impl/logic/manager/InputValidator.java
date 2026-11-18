package patmal.workshops.student.impl.logic.manager;

public class InputValidator
{
    private InputValidator()
    {
    }

    public static void checkId(String id, String kind)
    {
        if (id == null || id.trim().isEmpty())
        {
            throw new IllegalArgumentException("The id of a new " + kind + " cannot be empty");
        }
    }
}
