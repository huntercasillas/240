package service;

import model.User;
import model.Event;
import model.Person;
import access.UserDao;
import access.EventDao;
import access.PersonDao;
import error.ServerError;
import result.EventResult;
import request.LoadRequest;
import result.MessageResult;

/**
 * Class that contains all information for loading the database.
 */
public class LoadService {

    private EventDao eventDAO;
    private PersonDao personDAO;
    private UserDao userDAO;

    public LoadService() throws ServerError {
        try {
            eventDAO = new EventDao();
            personDAO = new PersonDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    public MessageResult loadInformation(LoadRequest loadRequest) {

        String serverResponse;

        int usersLoaded = 0;
        int personsLoaded = 0;
        int eventsLoaded = 0;

        try {
            for (User user : loadRequest.getUsers()) {
                userDAO.addUser(user);
                usersLoaded++;
            }
            for (Person person : loadRequest.getPersons()) {
                personDAO.addPerson(person);
                personsLoaded++;
            }
            for (EventResult event : loadRequest.getEvents()) {
                eventDAO.addEvent(new Event(event));
                eventsLoaded++;
            }

            serverResponse = "Successfully added " + usersLoaded + " users, " +
                    personsLoaded + " persons, and " + eventsLoaded + " events to the database.";
        } catch (ServerError e) {
            serverResponse = e.getMessage();
        }

        return new MessageResult(serverResponse);
    }
}