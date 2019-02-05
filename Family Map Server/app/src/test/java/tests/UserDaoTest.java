package tests;

import model.User;
import org.junit.*;
import access.UserDao;
import error.ServerError;
import request.RegisterRequest;
import static org.junit.Assert.*;

/**
 * Class for testing UserDao.
 */
public class UserDaoTest {

    private UserDao userDao;

    @Before
    public void setDefault() {
        try {
            userDao = new UserDao();
        } catch (ServerError e) {
            fail();
        }
    }

    @Test
    public void registerValidUserTest() throws ServerError {

        RegisterRequest registerRequest = new RegisterRequest("registerValidUser", "password",
                "email", "Test", "User", "f");

        try {
            String userID = userDao.registerUser(registerRequest);
        }
        catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void registerInvalidUserTest() {

        RegisterRequest registerRequest = new RegisterRequest("registerInvalidUser", "password",
                "email", "Test", "User", "error");

        try {
            String userID = userDao.registerUser(registerRequest);
        }
        catch (ServerError e) {
            return;
        }
    }

    @Test
    public void loginValidUserTest() throws ServerError {

        User testUser = new User("loginValidUser", "password", "email",
                "Test", "User", "f", "loginValidUser");
        boolean success;
        try {
            userDao.addUser(testUser);
            success = userDao.loginUser("loginValidUser", "password");
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        assertTrue(success);
    }

    @Test
    public void loginInvalidUserTest() throws ServerError {

        User testUser = new User("loginInvalidUser", "password", "email",
                "Test", "User", "f", "loginInvalidUser");
        boolean success;
        try {
            userDao.addUser(testUser);
            success = userDao.loginUser("loginInvalidUser", "error");
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        assertFalse(success);
    }

    @Test
    public void addValidUserTest() {

        User testUser = new User("addValidUser", "password", "email",
                "Test", "User", "f", "addValidUser");

        try {
            userDao.addUser(testUser);

        } catch (ServerError e) {
            fail();
        }
    }

    @Test
    public void addInvalidUserTest() {

        User testUser = new User("addInvalidUser", "password", "email",
                "Test", "User", "error", "addInvalidUser");

        try {
            userDao.addUser(testUser);

        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void findValidUserTest() {

        User testUser = new User("findValidUserTest", "password", "email",
                "Test", "User", "f", "findValidUserTestID");

        try {
            userDao.addUser(testUser);
            User foundUser = userDao.findUser("findValidUserTest");
        }
        catch (ServerError e) {
            fail();
        }
        return;
    }

    @Test
    public void findInvalidUserTest() {

        try {
            User testUser = userDao.findUser(null);
        }
        catch (ServerError e) {
            return;
        }
    }

    @Test
    public void clearUsersTest() {

        try {
            userDao.clearUsers();
        } catch (ServerError e) {
            fail();
        }
    }
}