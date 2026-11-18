package patmal.workshops.student.impl.logic.manager;

import patmal.workshops.api.model.Workshop;
import patmal.workshops.student.impl.model.InstructorImpl;
import patmal.workshops.student.impl.model.WorkshopImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkshopManager
{
    private final Map<Integer, WorkshopImpl> workshops = new LinkedHashMap<Integer, WorkshopImpl>();
    private int nextWorkshopId = 1;

    public WorkshopImpl createWorkshop(String name, String description)
    {
        WorkshopImpl workshop = new WorkshopImpl(nextWorkshopId, name, description);
        workshops.put(workshop.getId(), workshop);
        nextWorkshopId++;
        return workshop;
    }

    public WorkshopImpl getWorkshop(Integer id)
    {
        return workshops.get(id);
    }

    public WorkshopImpl getRegisteredWorkshop(Workshop workshop, String errorMessage)
    {
        if (workshop == null || !workshops.containsKey(workshop.getId()))
        {
            throw new IllegalArgumentException(errorMessage);
        }
        return workshops.get(workshop.getId());
    }

    public List<Workshop> getWorkshops()
    {
        return new ArrayList<Workshop>(workshops.values());
    }

    public void associateWithInstructor(WorkshopImpl workshop, InstructorImpl newInstructor)
    {
        InstructorImpl oldInstructor = (InstructorImpl) workshop.getInstructor();
        if (oldInstructor != null && oldInstructor != newInstructor)
        {
            oldInstructor.removeWorkshop(workshop);
        }

        workshop.setInstructor(newInstructor);
        newInstructor.addWorkshop(workshop);
    }
}
