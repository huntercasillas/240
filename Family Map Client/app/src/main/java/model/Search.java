package model;

import java.util.List;
import java.util.ArrayList;

/**
 * Class that contains all information for search results.
 */
public class Search {

    // Declare variables
    private List<Person> foundPersons;
    private List<Event> foundEvents;

    /**
     * Constructor.
     */
    public Search(String searchRequest) {
        Model model = Model.getModel();
        foundPersons = new ArrayList<>();
        foundEvents = new ArrayList<>();

        for (Person person : model.getAllPersons().values()) {
            String fullName = person.getFirstName().toLowerCase() + " " + person.getLastName().toLowerCase();

            if (person.getFirstName().toLowerCase().contains(searchRequest.toLowerCase())) {
                foundPersons.add(person);
            } else if (person.getLastName().toLowerCase().contains(searchRequest.toLowerCase())) {
                foundPersons.add(person);
            } else if (fullName.contains(searchRequest.toLowerCase())) {
                foundPersons.add(person);
            }
        }

        for (Event event : model.getFilteredEvents()) {
            Person foundPerson = model.getAllPersons().get(event.getPersonID());
            String fullName = foundPerson.getFirstName() + " " + foundPerson.getLastName();

            if (fullName.toLowerCase().contains(searchRequest.toLowerCase())) {
                foundEvents.add(event);
            } else if (event.getType().toLowerCase().contains(searchRequest.toLowerCase())) {
                foundEvents.add(event);
            } else if (event.getCity().toLowerCase().contains(searchRequest.toLowerCase())) {
                foundEvents.add(event);
            } else if (event.getCountry().toLowerCase().contains(searchRequest.toLowerCase())) {
                foundEvents.add(event);
            } else if ((event.getYear()).contains(searchRequest.toLowerCase())) {
                foundEvents.add(event);
            }
        }
    }

    public List<Event> getFoundEvents() {
        return foundEvents;
    }

    public void setFoundEvents(List<Event> matchingEvents) {
        this.foundEvents = matchingEvents;
    }

    public List<Person> getFoundPersons() {
        return foundPersons;
    }

    public void setFoundPersons(List<Person> matchingPeople) {
        this.foundPersons = matchingPeople;
    }
}