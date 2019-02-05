package tests;

import model.User;
import model.Event;
import org.junit.*;
import java.util.List;
import access.UserDao;
import access.EventDao;
import error.ServerError;

/**
 * Class for testing events service.
 */
public class EventsServiceTest {

    private EventDao eventDAO;
    private UserDao userDAO;

    @Before
    public void setDefault() throws ServerError {
        try {
            eventDAO = new EventDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    @Test
    public void findValidEventsTest() throws ServerError {

        User testUser = new User("eventsUser", "password", "email",
                "Test", "User", "f", "eventsUser");
        userDAO.addUser(testUser);

        Event testEvent = new Event("testEvents", "eventsUser", "eventsUser",
                "20.853", "-135.56", "United States",
                "Orem", "birth", "1994");
        Event testEvent2 = new Event("testEvents2", "eventsUser", "eventsUser",
                "20.853", "-135.56", "United States",
                "Orem", "death", "2018");
        eventDAO.addEvent(testEvent);
        eventDAO.addEvent(testEvent2);

        try {
            String foundUsername = testUser.getUsername();
            List<Event> foundEvents = eventDAO.findAllEvents(foundUsername);

        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void findInvalidEventsTest() throws ServerError {

        User testUser = new User("invalidEventsUser", "password", "email",
                "Test", "User", "f", "invalidEventsUser");
        userDAO.addUser(testUser);

        Event testEvent = new Event("invalidTestEvents", "invalidEventsUser",
                "invalidEventsUser2", "20.853", "-135.56",
                "United States", "Orem", "birth", "1994");
        Event testEvent2 = new Event("invalidTestEvents2", "invalidEventsUser",
                "invalidEventsUser", "20.853", "-135.56",
                "United States", "Orem", "death", "2018");
        eventDAO.addEvent(testEvent);
        eventDAO.addEvent(testEvent2);

        try {
            List<Event> foundEvents = eventDAO.findAllEvents("spellingError");

        } catch (ServerError e) {
            return;
        }
    }
}