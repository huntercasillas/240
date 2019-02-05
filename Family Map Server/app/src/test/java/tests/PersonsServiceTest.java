package tests;

import model.User;
import org.junit.*;
import model.Person;
import java.util.List;
import access.UserDao;
import access.PersonDao;
import error.ServerError;

/**
 * Class for testing persons service.
 */
public class PersonsServiceTest {

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
    public void findValidPersonsTest() throws ServerError {

        User testUser = new User("personsUser", "password", "email",
                "Test", "User", "f", "personsUser");
        userDAO.addUser(testUser);

        Person testPerson = new Person("personsUser", "personsUser", "Test",
                "User", "f", null, null, null);
        Person testPerson2 = new Person("personsUser2", "personsUser", "Test",
                "User", "f", null, null, null);
        personDAO.addPerson(testPerson);
        personDAO.addPerson(testPerson2);

        try {
            String foundUsername = testUser.getUsername();
            List<Person> foundPersons = personDAO.findAllPersons(foundUsername);

        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void findInvalidPersonsTest() throws ServerError {

        User testUser = new User("invalidPersonsUser", "password", "email",
                "Test", "User", "f", "invalidPersonsUser");
        userDAO.addUser(testUser);

        Person testPerson = new Person("invalidPersonsUser", "invalidPersonsUser", "Test",
                "User", "f", null, null, null);
        Person testPerson2 = new Person("invalidPersonsUser2", "invalidPersonsUser", "Test",
                "User", "f", null, null, null);
        personDAO.addPerson(testPerson);
        personDAO.addPerson(testPerson2);

        try {
            List<Person> foundPersons = personDAO.findAllPersons("spellingError");

        } catch (ServerError e) {
            return;
        }
    }
}