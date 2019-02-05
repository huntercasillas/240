package service;

import model.Person;
import access.PersonDao;
import error.ServerError;
import access.AuthTokenDao;
import result.PersonResult;

/**
 * Class that contains all information for finding a person in the database.
 */
public class PersonService {

    private AuthTokenDao authDAO;
    private PersonDao personDAO;

    public PersonService() throws ServerError {
        try {
            authDAO = new AuthTokenDao();
            personDAO = new PersonDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    public PersonResult findPerson(String authToken, String personID) throws ServerError {

        String foundUsername;
        Person foundPerson;

        try {
            foundUsername = authDAO.findUsername(authToken);
            foundPerson = personDAO.findPerson(personID);

            if (!foundUsername.equals(foundPerson.getDescendant())) {
                throw new ServerError("Error. The person does not belong to the current user.");
            }
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return new PersonResult(foundPerson);
    }
}