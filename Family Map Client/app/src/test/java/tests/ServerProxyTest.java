package tests;

import org.junit.Test;
import org.junit.Before;
import model.Event;
import model.Person;
import java.util.List;
import net.ServerProxy;
import result.LoginResult;
import request.LoginRequest;
import result.RegisterResult;
import request.RegisterRequest;
import static org.junit.Assert.*;

/**
 * Automated Tests - for each method, you should test both success and failure cases.
 * Write JUnit tests to verify that your Server Proxy class works correctly:
 * Logging in an existing user.
 * Registering a new user.
 * Retrieving people.
 * Retrieving events.
 */
public class ServerProxyTest {

    private ServerProxy testServerProxy;
    private String testServerHost = "192.168.0.121";
    private String testServerPort = "8080";

    @Before
    public void setDefault() {
        testServerProxy = new ServerProxy();
        RegisterRequest testRegisterRequest = new RegisterRequest("huntercasillas", "pass",
                "huntercasillas@me.com", "Hunter", "Casillas", "m");
        RegisterResult testRegisterResult = testServerProxy.registerUser(testServerHost, testServerPort, testRegisterRequest);
    }

    @Test
    public void registerNewUserTest() {
        RegisterRequest testRegisterRequest = new RegisterRequest("huntercas123", "password",
                "huntercasillas@me.com", "Hunter", "Casillas", "m");

        RegisterResult testRegisterResult = testServerProxy.registerUser(testServerHost, testServerPort, testRegisterRequest);

        assertNotNull(testRegisterResult.getAuthToken());
    }

    @Test
    public void registerExistingUserTest() {
        RegisterRequest testRegisterRequest = new RegisterRequest("huntercasillas", "pass",
                "huntercasillas@me.com", "Hunter", "Casillas", "m");

        RegisterResult testRegisterResult = testServerProxy.registerUser(testServerHost, testServerPort, testRegisterRequest);

        assertNull(testRegisterResult.getAuthToken());
    }

    @Test
    public void loginCorrectUserTest() {
        LoginRequest testLoginRequest = new LoginRequest("huntercasillas", "pass");

        LoginResult testLoginResult = testServerProxy.loginUser(testServerHost, testServerPort, testLoginRequest);

        assertNotNull(testLoginResult.getAuthToken());
    }

    @Test
    public void loginIncorrectUserTest() {
        LoginRequest testLoginRequest = new LoginRequest("abc", "abc123");

        LoginResult testLoginResult = testServerProxy.loginUser(testServerHost, testServerPort, testLoginRequest);

        assertNull(testLoginResult.getAuthToken());
    }

    @Test
    public void getEventsTest() {
        List<Event> events = testServerProxy.getEvents(testServerHost, testServerPort);
        assertNotNull(events);
    }

    @Test
    public void getInvalidEventsTest() {
        List<Event> events = null;
        String incorrectServerHost = "192.168.0.000";

        try {
            events = testServerProxy.getEvents(incorrectServerHost, testServerPort);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNull(events);
    }

    @Test
    public void getPersonsTest() {
        List<Person> persons = testServerProxy.getPersons(testServerHost, testServerPort);
        assertNotNull(persons);
    }

    @Test
    public void getInvalidPersonsTest() {
        List<Person> persons = null;
        String incorrectServerHost = "192.168.0.000";

        try {
            persons = testServerProxy.getPersons(incorrectServerHost, testServerPort);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNull(persons);
    }
}