package tests;

import model.User;
import model.Event;
import org.junit.*;
import access.UserDao;
import access.EventDao;
import error.ServerError;

/**
 * Class for testing event service.
 */
public class EventServiceTest {

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
    public void findValidEventTest() throws ServerError {

        User testUser = new User("eventUser", "password", "email",
                "Test", "User", "f", "eventUser");
        userDAO.addUser(testUser);

        Event testEvent = new Event("testEvent", "eventUser", "eventUser",
                "20.853", "-135.56", "United States",
                "Orem", "birth", "1994");
        eventDAO.addEvent(testEvent);

        try {
            String foundUsername = testUser.getUsername();
            Event foundEvent = eventDAO.findEvent("testEvent");

            if (!foundUsername.equals(foundEvent.getDescendant())) {
                throw new ServerError("Error. The event does not belong to the current user.");
            }
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void findInvalidEventTest() {

        User testUser = new User("invalidEventUser", "password", "email",
                "Test", "User", "f", "invalidEventUser");

        Event testEvent = new Event("invalidTestEvent", "invalidEventUser",
                "invalidEventUser", "20.853", "-135.56",
                "United States", "Orem", "birth", "1994");

        try {
            userDAO.addUser(testUser);
            eventDAO.addEvent(testEvent);
            String foundUsername = testUser.getUsername();
            Event foundEvent = eventDAO.findEvent("spellingErrorHere");

            if (!foundUsername.equals(foundEvent.getDescendant())) {
                throw new ServerError("Error. The event does not belong to the current user.");
            }
        } catch (ServerError e) {
            return;
        }
    }
}