package service;

import model.Event;
import java.util.List;
import access.EventDao;
import error.ServerError;
import access.AuthTokenDao;
import result.EventsResult;

/**
 * Class that contains all information for finding events in the database.
 */
public class EventsService {

    private AuthTokenDao authDAO;
    private EventDao eventDAO;

    public EventsService() throws ServerError {
        try {
            authDAO = new AuthTokenDao();
            eventDAO = new EventDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    public EventsResult findEvents(String authToken) throws ServerError {

        String foundUsername;
        List<Event> foundEvents;

        try {
            foundUsername = authDAO.findUsername(authToken);
            foundEvents = eventDAO.findAllEvents(foundUsername);

        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return new EventsResult(foundEvents);
    }
}