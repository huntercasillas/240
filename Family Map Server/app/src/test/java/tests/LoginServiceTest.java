package tests;

import model.User;
import org.junit.*;
import access.UserDao;
import error.ServerError;
import access.AuthTokenDao;

/**
 * Class for testing login service.
 */
public class LoginServiceTest {

    private AuthTokenDao authDAO;
    private UserDao userDAO;

    @Before
    public void setDefault() throws ServerError {
        try {
            authDAO = new AuthTokenDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    @Test
    public void loginValidUserTest() throws ServerError {

        User testUser = new User("loginUser", "password", "email",
                "Test", "User", "f", "loginUser");
        userDAO.addUser(testUser);

        try {
            userDAO.loginUser("loginUser", "password");
        }
        catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void loginInvalidUserTest() {

        try {
            userDAO.loginUser("loginInvalidUser", "error");
        }
        catch (ServerError e) {
            return;
        }
    }
}