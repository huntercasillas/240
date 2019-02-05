package tests;

import model.User;
import model.Event;
import org.junit.*;
import access.UserDao;
import java.util.List;
import access.EventDao;
import error.ServerError;
import static org.junit.Assert.*;

/**
 * Class for testing EventDao.
 */
public class EventDaoTest {

    private EventDao eventDAO;
    private UserDao userDAO;

    @Before
    public void setDefault() {
        try {
            eventDAO = new EventDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            fail();
        }
    }

    @Test
    public void addValidEventTest() {

        User testUser = new User("addValidEvent", "password", "email",
                "Test", "User", "f", "addValidEvent");

        Event testEvent = new Event("validTestEvent", "addValidEvent",
                "addValidEvent", "20.853", "-135.56",
                "United States", "Orem", "birth", "1994");

        try {
            userDAO.addUser(testUser);
            eventDAO.addEvent(testEvent);
        } catch (ServerError e) {
            fail();
        }
        return;
    }

    @Test
    public void addInvalidEventTest() {

        User testUser = new User("addInvalidEvent", "password", "email",
                "Test", "User", "f", "addInvalidEvent");

        Event testEvent = new Event("invalidTestEvent", "addInvalidEvent",
                "addValidEvent", "20.853", "-135.56",
                "United States", "Orem", "birth", "1994");

        try {
            userDAO.addUser(testUser);
            eventDAO.addEvent(testEvent);
        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void removeValidDescendantEventsTest() {

        User testUser = new User("validDescendantEvents", "password", "email",
                "Test", "User", "f", "validDescendantEvents");

        Event testEvent = new Event("validDescendantEventsID", "validDescendantEvents",
                "validDescendantEvents", "20.853", "-135.56",
                "United States", "Orem", "birth", "1994");

        try {
            userDAO.addUser(testUser);
            eventDAO.addEvent(testEvent);
            eventDAO.removeDescendantEvents("validDescendantEvents");
        } catch (ServerError e) {
            fail();
        }
        return;
    }

    @Test
    public void removeInvalidDescendantEventsTest() {

        User testUser = new User("invalidDescendantEvents", "password", "email",
                "Test", "User", "f", "invalidDescendantEvents");

        Event testEvent = new Event("invalidDescendantEventsID", "invalidDescendantEvents",
                "invalidDescendantEvents", "20.853", "-135.56", "United States",
                "Orem", "birth", "1994");

        try {
            userDAO.addUser(testUser);
            eventDAO.addEvent(testEvent);
            eventDAO.removeDescendantEvents("spellingError");
        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void findValidEventTest() throws ServerError {

        User testUser = new User("findValidEvent", "password", "email",
                "Test", "User", "f", "findValidEvent");
        userDAO.addUser(testUser);

        Event testEvent = new Event("findValidEventID", "findValidEvent", "findValidEvent",
                "20.853", "-135.56", "United States",
                "Orem", "birth", "1994");
        eventDAO.addEvent(testEvent);

        try {
            String foundUsername = testUser.getUsername();
            Event foundEvent = eventDAO.findEvent("findValidEventID");

            if (!foundUsername.equals(foundEvent.getDescendant())) {
                throw new ServerError("Error. The event does not belong to the current user.");
            }
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void findInvalidEventTest() throws ServerError {

        User testUser = new User("findInvalidEvent", "password", "email",
                "Test", "User", "f", "findInvalidEvent");
        userDAO.addUser(testUser);

        Event testEvent = new Event("findInvalidEventID", "findInvalidEvent",
                "invalidEventUser", "20.853", "-135.56",
                "United States", "Orem", "birth", "1994");
        eventDAO.addEvent(testEvent);

        try {
            String foundUsername = testUser.getUsername();
            Event foundEvent = eventDAO.findEvent("spellingErrorHere");

        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void findAllValidEventsTest() {

        User testUser = new User("findValidEvents", "password", "email",
                "Test", "User", "f", "findValidEvents");

        Event testEvent = new Event("findValidID", "findValidEvents", "findValidEvents",
                "20.853", "-135.56", "United States",
                "Orem", "birth", "1994");
        Event testEvent2 = new Event("findValidID2", "findValidEvents", "findValidEvents",
                "20.853", "-135.56", "United States",
                "Orem", "death", "2018");


        try {
            userDAO.addUser(testUser);
            eventDAO.addEvent(testEvent);
            eventDAO.addEvent(testEvent2);
            String foundUsername = testUser.getUsername();
            List<Event> foundEvents = eventDAO.findAllEvents(foundUsername);

        } catch (ServerError e) {
            fail();
        }
        return;
    }

    @Test
    public void findAllInvalidEventsTest() {

        User testUser = new User("findInvalidEvents", "password", "email",
                "Test", "User", "f", "findInvalidEvents");

        Event testEvent = new Event("findInvalidID", "findInvalidEvents",
                "invalidEventsUser2", "20.853", "-135.56",
                "United States", "Orem", "birth", "1994");
        Event testEvent2 = new Event("findInvalidID2", "findInvalidEvents",
                "invalidEventsUser", "20.853", "-135.56",
                "United States", "Orem", "death", "2018");

        try {
            userDAO.addUser(testUser);
            eventDAO.addEvent(testEvent);
            eventDAO.addEvent(testEvent2);
            List<Event> foundEvents = eventDAO.findAllEvents("spellingError");

        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void clearEventsTest() {

        try {
            eventDAO.clearEvents();
        } catch (ServerError e) {
            fail();
        }
    }
}