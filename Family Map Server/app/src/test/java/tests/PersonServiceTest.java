package tests;

import model.User;
import org.junit.*;
import model.Person;
import access.UserDao;
import access.PersonDao;
import error.ServerError;

/**
 * Class for testing person service.
 */
public class PersonServiceTest {

    private PersonDao personDAO;
    private UserDao userDAO;

    @Before
    public void setDefault() throws ServerError {
        try {
            personDAO = new PersonDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    @Test
    public void findValidPersonTest() throws ServerError {

        User testUser = new User("personUser", "password", "email",
                "Test", "User", "f", "personUser");
        userDAO.addUser(testUser);

        Person testPerson = new Person("personUser", "personUser", "Test",
                "User", "f", null, null, null);
        personDAO.addPerson(testPerson);

        try {
            String foundUsername = testUser.getUsername();
            Person foundPerson = personDAO.findPerson("personUser");

            if (!foundUsername.equals(foundPerson.getDescendant())) {
                throw new ServerError("Error. The person does not belong to the current user.");
            }
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test (expected = ServerError.class)
    public void findInvalidPersonTest() throws ServerError {

        User testUser = new User("invalidPersonUser", "password", "email",
                "Test", "User", "f", "invalidPersonUser");
        userDAO.addUser(testUser);

        try {
            String foundUsername = testUser.getUsername();
            Person foundPerson = personDAO.findPerson("spellingError");

            if (!foundUsername.equals(foundPerson.getDescendant())) {
                throw new ServerError("Error. The person does not belong to the current user.");
            }
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }
}