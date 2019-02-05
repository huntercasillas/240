package service;

import access.*;
import error.ServerError;
import result.MessageResult;

/**
 * Class that contains all information for clearing the database.
 */
public class ClearService {

    private AuthTokenDao authDAO;
    private EventDao eventDAO;
    private PersonDao personDAO;
    private UserDao userDAO;

    public ClearService() throws ServerError {
        try {
            authDAO = new AuthTokenDao();
            eventDAO = new EventDao();
            personDAO = new PersonDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    public MessageResult clearData() {

        MessageResult serverResponse = new MessageResult("Clear successful.");

        try {
            authDAO.clearAuthTokens();
            eventDAO.clearEvents();
            personDAO.clearPersons();
            userDAO.clearUsers();
        } catch (ServerError e) {
            serverResponse = new MessageResult(e.getMessage());
        } catch (IllegalArgumentException e) {
            serverResponse = new MessageResult("Error. Could not clear database.");
        }

        return serverResponse;
    }
}