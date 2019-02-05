package service;

import model.Person;
import java.util.List;
import access.PersonDao;
import error.ServerError;
import access.AuthTokenDao;
import result.PersonsResult;

/**
 * Class that contains all information for finding persons in the database.
 */
public class PersonsService {

    private AuthTokenDao authDAO;
    private PersonDao personDAO;

    public PersonsService() throws ServerError {
        try {
            authDAO = new AuthTokenDao();
            personDAO = new PersonDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    public PersonsResult findPersons(String authToken) throws ServerError {

        String foundUsername;
        List<Person> foundPersons;

        try {
            foundUsername = authDAO.findUsername(authToken);
            foundPersons = personDAO.findAllPersons(foundUsername);

        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return new PersonsResult(foundPersons);
    }
}