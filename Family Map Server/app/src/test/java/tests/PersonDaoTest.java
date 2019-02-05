package tests;

import model.User;
import org.junit.*;
import model.Person;
import java.util.List;
import access.UserDao;
import access.PersonDao;
import error.ServerError;
import static org.junit.Assert.*;

/**
 * Class for testing PersonDao.
 */
public class PersonDaoTest {

    private PersonDao personDao;
    private UserDao userDao;

    @Before
    public void setDefault() {
        try {
            personDao = new PersonDao();
            userDao = new UserDao();
        } catch (ServerError e) {
            fail();
        }
    }

    @Test
    public void addValidPersonTest() throws ServerError {

        User testUser = new User("addValidPerson", "password", "email",
                "Test", "User", "f", "addValidPerson");

        try {
            userDao.addUser(testUser);

        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void addInvalidPersonTest() {

        User testUser = new User("", "password", "email",
                "Test", "User", "f", "addInvalidPerson");

        try {
            userDao.addUser(testUser);

        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void removeValidDescendantsTest() {

        User testUser = new User("removeValidDescendants", "password", "email",
                "Test", "User", "f", "removeValidDescendants");

        try {
            userDao.addUser(testUser);
            personDao.removeDescendants("removeValidDescendants");

        } catch (ServerError e) {
            fail();
        }
        return;
    }

    @Test
    public void removeInvalidDescendantsTest() {

        User testUser = new User("removeInvalidDescendants", "password", "email",
                "Test", "User", "f", "removeInvalidDescendants");

        try {
            userDao.addUser(testUser);
            personDao.removeDescendants("spellingError");

        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void findValidPersonTest() throws ServerError {

        User testUser = new User("findValidPerson", "password", "email",
                "Test", "User", "f", "findValidPerson");

        Person testPerson = new Person("findValidPerson", "findValidPerson", "Test",
                "User", "f", null, null, null);

        try {
            userDao.addUser(testUser);
            personDao.addPerson(testPerson);
            String foundUsername = testUser.getUsername();
            Person foundPerson = personDao.findPerson(foundUsername);

        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void findInvalidPersonTest() {

        User testUser = new User("findInvalidPerson", "password", "email",
                "Test", "User", "f", "findInvalidPerson");

        try {
            userDao.addUser(testUser);
            String foundUsername = testUser.getUsername();
            Person foundPerson = personDao.findPerson("spellingError");
        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void findValidAllPersonsTest() throws ServerError {

        User testUser = new User("findValidPersons", "password", "email",
                "Test", "User", "f", "findValidPersons");

        Person testPerson = new Person("findValidPersons", "findValidPersons", "Test",
                "User", "f", null, null, null);
        Person testPerson2 = new Person("findValidPersons2", "findValidPersons", "Test",
                "User", "f", null, null, null);

        try {
            userDao.addUser(testUser);
            personDao.addPerson(testPerson);
            personDao.addPerson(testPerson2);
            String foundUsername = testUser.getUsername();
            List<Person> foundPersons = personDao.findAllPersons(foundUsername);

        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void findInvalidAllPersonsTest() {

        User testUser = new User("findInvalidPersons", "password", "email",
                "Test", "User", "f", "findInvalidPersons");

        Person testPerson = new Person("findInvalidPersons", "findInvalidPersons", "Test",
                "User", "f", null, null, null);
        Person testPerson2 = new Person("findInvalidPersons2", "findInvalidPersons", "Test",
                "User", "f", null, null, null);


        try {
            userDao.addUser(testUser);
            personDao.addPerson(testPerson);
            personDao.addPerson(testPerson2);
            List<Person> foundPersons = personDao.findAllPersons("spellingError");

        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void clearPersonsTest() {

        try {
            personDao.clearPersons();
        } catch (ServerError e) {
            fail();
        }
    }
}