package request;

import model.*;
import result.*;
import java.util.List;

/**
 * Class that contains all information for a load request.
 */
public class LoadRequest {

    private List<User> users;
    private List<Person> persons;
    private List<EventResult> events;

    public LoadRequest(List<User> users, List<Person> persons, List<EventResult> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<EventResult> getEvents() {
        return events;
    }
}