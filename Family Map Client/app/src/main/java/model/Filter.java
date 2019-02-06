package model;

import java.util.List;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * Class that contains all information for filters.
 */
public class Filter {

    private Model model;
    private boolean filterStatus;
    private String filterType;
    private TreeMap<String, Filter> filters = new TreeMap<>();
    private TreeMap<String, Person> allPersons = new TreeMap<>();
    private TreeMap<String, Person> motherSideList = new TreeMap<>();
    private TreeMap<String, Person> fatherSideList = new TreeMap<>();

    /**
     * Constructor.
     */
    public Filter() {
        filterStatus = false;
    }

    private Filter(String filterType) {
        filterStatus = true;
        this.filterType = filterType;
    }

    public void createFilters() {

        model = Model.getModel();

        if (model.getFilters().isEmpty()) {
            List<String> eventTypes = getEventTypes();

            filters.clear();
            filters.put("Father's Side", new Filter("Father's Side"));
            filters.put("Mother's Side", new Filter("Mother's Side"));
            filters.put("Male Events", new Filter("Male Events"));
            filters.put("Female Events", new Filter("Female Events"));

            for (String eventType : eventTypes) {
                filters.put(eventType + " Events", new Filter(eventType + " Events"));
            }

            model.setFilters(filters);

        } else {
            filters = model.getFilters();
        }
    }

    public void applyFilters() {

        model = Model.getModel();
        String mainPersonID = model.getMainPersonID();
        allPersons = model.getAllPersons();
        TreeMap<String, Event> allEvents = model.getAllEvents();
        HashSet<Event> filteredEvents = new HashSet<>();
        TreeMap<String, Event> updatedFilteredEvents =  new TreeMap<>();
        filteredEvents.clear();

        Person mainPerson = model.getMainPerson();

        if (allPersons.containsKey(mainPerson.getMother()) && mainPerson.getMother() != null) {
            Person mother = allPersons.get(mainPerson.getMother());
            motherSideList.put(mother.getPersonID(), mother);

            if (allPersons.containsKey(mother.getMother()) && mother.getMother() != null) {
                getParents(mother, motherSideList);
            }
        }

        if (allPersons.containsKey(mainPerson.getFather()) && mainPerson.getFather() != null) {
            Person father = allPersons.get(mainPerson.getFather());
            fatherSideList.put(father.getPersonID(), father);

            if (allPersons.containsKey(father.getFather()) && father.getFather() != null) {
                getParents(father, fatherSideList);
            }
        }

        if (!filters.get("Father's Side").getFilterStatus() && filters.get("Mother's Side").getFilterStatus()) {
            for (Event event : allEvents.values()) {
                String personID = event.getPersonID();
                if (personID.equals(mainPersonID)) {
                    updatedFilteredEvents.put(event.getEventID(), event);
                }
                if (motherSideList.containsKey(event.getPersonID())) {
                    updatedFilteredEvents.put(event.getEventID(), event);
                }
            }
        } else if (filters.get("Father's Side").getFilterStatus() && !filters.get("Mother's Side").getFilterStatus()) {
            for (Event event : allEvents.values()) {
                String personID = event.getPersonID();
                if (personID.equals(mainPersonID)) {
                    updatedFilteredEvents.put(event.getEventID(), event);
                }
                if (fatherSideList.containsKey(event.getPersonID())) {
                    updatedFilteredEvents.put(event.getEventID(), event);
                }
            }
        } else if (!filters.get("Father's Side").getFilterStatus() && !filters.get("Mother's Side").getFilterStatus()) {
            for (Event event : allEvents.values()) {
                String personID = event.getPersonID();
                if (personID.equals(mainPersonID)) {
                    updatedFilteredEvents.put(event.getEventID(), event);
                }
            }
        } else {
            updatedFilteredEvents = allEvents;
        }

        for (Event event : updatedFilteredEvents.values()) {
            String gender = model.getGender(event.getPersonID());

            if (!filters.get(event.getType() + " Events").getFilterStatus()) {
                continue;
            }
            if (!filters.get("Male Events").getFilterStatus() && gender.equals("m")) {
                continue;
            }
            if (!filters.get("Female Events").getFilterStatus() && gender.equals("f")) {
                continue;
            }
            filteredEvents.add(event);
        }

        model.setFilteredEvents(filteredEvents);
        model.setFatherSideList(fatherSideList);
        model.setMotherSideList(motherSideList);
    }

    private void getParents(Person person, TreeMap<String, Person> parentSideList) {

        if (person.getFather() != null && allPersons.containsKey(person.getFather())) {
            Person father = allPersons.get(person.getFather());
            parentSideList.put(father.getPersonID(), father);
            getParents(father, parentSideList);
        }
        if (person.getMother() != null && allPersons.containsKey(person.getMother())) {
            Person mother = allPersons.get(person.getMother());
            parentSideList.put(mother.getPersonID(), mother);
            getParents(mother, parentSideList);
        }
    }

    public boolean getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(boolean status) {
        filterStatus = status;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    private List<String> getEventTypes() {

        model = Model.getModel();
        List<String> eventTypesList = new ArrayList<>();

        for (Event event : model.getAllEvents().values()) {
            if (!eventTypesList.contains(event.getType())) {
                eventTypesList.add(event.getType());
            }
        }
        return eventTypesList;
    }
}