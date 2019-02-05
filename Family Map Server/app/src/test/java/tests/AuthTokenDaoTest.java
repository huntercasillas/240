package tests;

import org.junit.*;
import error.ServerError;
import access.AuthTokenDao;
import static org.junit.Assert.*;

/**
 * Class for testing AuthTokenDao.
 */
public class AuthTokenDaoTest {

    private AuthTokenDao authTokenDao;

    @Before
    public void setDefault() {
        try {
            authTokenDao = new AuthTokenDao();
        } catch (ServerError e) {
            fail();
        }
    }

    @Test
    public void addValidTokenTest() {

        try {
            String newToken = authTokenDao.addAuthToken("hunter");

        } catch (ServerError e) {
            fail();
        }
    }

    @Test
    public void addInvalidTokenTest() {

        try {
            authTokenDao.addAuthToken("");

        } catch (ServerError e) {
            return;
        }
    }

    @Test
    public void findValidUsernameTest() {

        try {
            String authToken = authTokenDao.addAuthToken("authUser");
            String username = authTokenDao.findUsername(authToken);
        }
        catch (ServerError e) {
            fail();
        }
        return;
    }

    @Test
    public void findInvalidUsernameTest() {

        try {
            authTokenDao.findUsername(null);

        } catch (ServerError | AssertionError e) {
            return;
        }
    }

    @Test
    public void clearAuthTokensTest() {

        try {
            authTokenDao.clearAuthTokens();
        } catch (ServerError e) {
            fail();
        }
    }
}