package tests;

import model.User;
import model.Event;
import org.junit.*;
import model.Person;
import access.UserDao;
import java.util.List;
import access.EventDao;
import access.PersonDao;
import error.ServerError;
import result.EventResult;
import java.util.ArrayList;
import request.LoadRequest;

/**
 * Class for testing load service.
 */
public class LoadServiceTest {

    private EventDao eventDAO;
    private PersonDao personDAO;
    private UserDao userDAO;

    @Before
    public void setDefault() throws ServerError {
        try {
            eventDAO = new EventDao();
            personDAO = new PersonDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    @Test
    public void loadValidInformationTest() throws ServerError {

        List<User> users = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        List<EventResult> events = new ArrayList<>();

        User testUser = new User("loadUser", "password", "email",
                "Test", "User", "f", "loadUser");
        users.add(testUser);

        Person testPerson = new Person("loadUser", "loadUser", "Test",
                "User", "f", null, null, null);
        persons.add(testPerson);

        Event testEvent = new Event("loadEvent", "loadUser", "loadUser",
                "20.853", "-135.56", "United States",
                "Orem", "birth", "1994");
        EventResult eventResult = new EventResult(testEvent);
        events.add(eventResult);

        LoadRequest loadRequest = new LoadRequest(users, persons, events);

        try {
            for (User user : loadRequest.getUsers()) {
                userDAO.addUser(user);
            }
            for (Person person : loadRequest.getPersons()) {
                personDAO.addPerson(person);
            }
            for (EventResult event : loadRequest.getEvents()) {
                eventDAO.addEvent(new Event(event));
            }
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void loadInvalidInformationTest() {

        List<User> users = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        List<EventResult> events = new ArrayList<>();

        User testUser = new User("loadInvalidUser", "password", "email",
                "Test", "User", "error", "loadInvalidUser");
        users.add(testUser);

        Person testPerson = new Person("loadInvalidUser", "loadInvalidUser", "Test",
                "User", "error", null, null, null);
        persons.add(testPerson);

        Event testEvent = new Event("loadEvent", "loadInvalidUser", "loadInvalidUser",
                "20.853", "-135.56", "United States",
                "Orem", "birth", "1994");
        EventResult eventResult = new EventResult(testEvent);
        events.add(eventResult);

        LoadRequest loadRequest = new LoadRequest(users, persons, events);

        try {
            for (User user : loadRequest.getUsers()) {
                userDAO.addUser(user);
            }
            for (Person person : loadRequest.getPersons()) {
                personDAO.addPerson(person);
            }
            for (EventResult event : loadRequest.getEvents()) {
                eventDAO.addEvent(new Event(event));
            }

        } catch (ServerError e) {
            return;
        }
    }
}