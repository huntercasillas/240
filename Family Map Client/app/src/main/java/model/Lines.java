package model;

import java.util.TreeSet;
import java.util.SortedSet;

/**
 * Class that contains all information for relationship lines.
 */
public class Lines {

    private TreeSet<Event> lifeStoryLines, spouseLines, fatherSideLines, motherSideLines;

    /**
     * Constructor.
     */
    public Lines(Event event) {

        // Declare and initialize variables
        Person currentPerson;
        Model model = Model.getModel();
        lifeStoryLines = new TreeSet<>();
        spouseLines = new TreeSet<>();
        fatherSideLines = new TreeSet<>();
        motherSideLines = new TreeSet<>();

        // Get the selected event's person
        try {
            currentPerson = model.getAllPersons().get(event.getPersonID());
        } catch (Exception e) {
            return;
        }

        // Set up life story lines
        for (Event selectedEvent : model.getFilteredEvents()) {
            if (selectedEvent.getPersonID().equals(event.getPersonID())) {
                lifeStoryLines.add(selectedEvent);
            }
        }

        // Set up family tree lines
        String fatherID = currentPerson.getFather();
        String motherID = currentPerson.getMother();

        // Add family tree lines recursively
        if (fatherID != null) {
            makeFatherLines(fatherID, event);
        }
        if (motherID != null) {
            makeMotherLines(motherID, event);
        }

        // Set up spouse lines
        try {
            if (currentPerson.getSpouse() == null) {
                return;
            }
            String spouseID = currentPerson.getSpouse();

            if (spouseID == null) {
                return;
            }

            // Add spouse lines for selected event
            for (Event selectedEvent : model.getFilteredEvents()) {
                if (selectedEvent.getPersonID().equals(spouseID)) {
                    spouseLines.add(selectedEvent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SortedSet<Event> getLifeStoryLines() {
        return lifeStoryLines;
    }

    public TreeSet<Event> getSpouseLines() {
        return spouseLines;
    }

    public SortedSet<Event> getFatherSideLines() {
        return fatherSideLines;
    }

    public SortedSet<Event> getMotherSideLines() {
        return motherSideLines;
    }

    private void makeFatherLines(String parentID, Event currentEvent) {

        // Get current model
        Model model = Model.getModel();

        // Declare and add to the list of father events
        TreeSet<Event> fatherEvents = new TreeSet<>();
        for (Event event : model.getFilteredEvents()) {
            if (event.getPersonID().equals(parentID)) {
                fatherEvents.add(event);
            }
        }
        // If the list of father events is empty, return
        if (fatherEvents.size() == 0) {
            return;
        }

        Event startingEvent = fatherEvents.first();

        fatherSideLines.add(currentEvent);
        fatherSideLines.add(startingEvent);

        // Get the current person's father
        Person currentPerson = model.getAllPersons().get(startingEvent.getPersonID());
        String fatherID = currentPerson.getFather();

        // Recursively add more father lines
        if (fatherID != null) {
            makeFatherLines(fatherID, startingEvent);
        }
    }

    private void makeMotherLines(String parentID, Event currentEvent) {

        // Get current model
        Model model = Model.getModel();

        // Declare and add to the list of mother events
        TreeSet<Event> motherEvents = new TreeSet<>();
        for (Event event : model.getFilteredEvents()) {
            if (event.getPersonID().equals(parentID)) {
                motherEvents.add(event);
            }
        }
        // If the list of mother events is empty, return
        if (motherEvents.size() == 0) {
            return;
        }

        Event startingEvent = motherEvents.first();

        motherSideLines.add(currentEvent);
        motherSideLines.add(startingEvent);

        // Get the current person's mother
        Person currentPerson = model.getAllPersons().get(startingEvent.getPersonID());
        String motherID = currentPerson.getMother();

        // Recursively add more mother lines
        if (motherID != null) {
            makeMotherLines(motherID, startingEvent);
        }
    }
}