package service;

import model.Event;
import access.EventDao;
import error.ServerError;
import result.EventResult;
import access.AuthTokenDao;

/**
 * Class that contains all information for finding an event in the database.
 */
public class EventService {

    private AuthTokenDao authDAO;
    private EventDao eventDAO;

    public EventService() throws ServerError {
        try {
            authDAO = new AuthTokenDao();
            eventDAO = new EventDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    public EventResult findEvent(String authToken, String eventID) throws ServerError {

        String foundUsername;
        Event foundEvent;

        try {
            foundUsername = authDAO.findUsername(authToken);
            foundEvent = eventDAO.findEvent(eventID);

            if (!foundUsername.equals(foundEvent.getDescendant())) {
                throw new ServerError("Error. The event does not belong to the current user.");
            }
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return new EventResult(foundEvent);
    }
}