package tests;

import org.junit.*;
import error.ServerError;
import result.MessageResult;
import service.ClearService;
import static org.junit.Assert.*;

/**
 * Class for testing clear service.
 */
public class ClearServiceTest {

    @Test
    public void clearValidDataTest() {

        MessageResult serverResponse;
        MessageResult expectedResponse = new MessageResult("Clear successful.");

        try {
            ClearService clearService = new ClearService();
            serverResponse = clearService.clearData();
        } catch (ServerError e) {
            serverResponse = new MessageResult(e.getMessage());
        }

        if (serverResponse.equals(expectedResponse)) {
            return;
        }
    }

    @Test
    public void clearInvalidDataTest() {

        MessageResult serverResponse;
        MessageResult invalidResponse = new MessageResult("Error.");

        try {
            ClearService clearService = new ClearService();
            serverResponse = clearService.clearData();
        } catch (ServerError e) {
            serverResponse = new MessageResult(e.getMessage());
        }

        if (serverResponse.equals(invalidResponse)) {
            fail();
        }
    }
}