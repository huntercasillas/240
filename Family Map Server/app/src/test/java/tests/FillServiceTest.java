package tests;

import org.junit.*;
import access.UserDao;
import error.ServerError;
import service.FillService;
import result.MessageResult;
import request.RegisterRequest;
import static org.junit.Assert.*;

/**
 * Class for testing fill service.
 */
public class FillServiceTest {

    private UserDao userDAO;

    @Before
    public void setDefault() throws ServerError {
        try {
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    @Test
    public void fillValidInformationTest() throws ServerError {

        RegisterRequest registerRequest = new RegisterRequest("fillUser", "password", "email",
                "Test", "User", "f");
        try {
            userDAO.registerUser(registerRequest);
            FillService fillService = new FillService();
            MessageResult serverResponse = fillService.fillInformation("fillUser", 4);

        } catch (ServerError e) {
            fail();
        }
        return;
    }

    @Test
    public void fillInvalidInformationTest() {

        RegisterRequest registerRequest = new RegisterRequest("fillInvalidUser",
                "password", "email", "Test", "User", "f");
        try {
            userDAO.registerUser(registerRequest);
            FillService fillService = new FillService();
            MessageResult serverResponse = fillService.fillInformation("spellingError", 4);

        } catch (ServerError | NullPointerException e) {
            return;
        }
    }
}