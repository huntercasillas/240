package tests;

import org.junit.*;
import access.UserDao;
import error.ServerError;
import access.AuthTokenDao;
import request.RegisterRequest;

/**
 * Class for testing register service.
 */
public class RegisterServiceTest {

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
    public void registerValidUserTest() throws ServerError {

        RegisterRequest registerRequest = new RegisterRequest("registerUser", "password",
                "email", "Test", "User", "f");

        try {
            String userID = userDAO.registerUser(registerRequest);
            String authToken = authDAO.addAuthToken(registerRequest.getUsername());
        }
        catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
        return;
    }

    @Test
    public void registerInvalidUserTest() throws ServerError {

        RegisterRequest registerRequest = new RegisterRequest("invalidRegisterUser",
                "", "email", "Test", "User", "error");

        try {
            String userID = userDAO.registerUser(registerRequest);
            String authToken = authDAO.addAuthToken(registerRequest.getUsername());
        }
        catch (ServerError e) {
            return;
        }
    }
}