package error;

/**
 * Class for server error handling.
 */
public class ServerError extends Exception {

    public ServerError() {
        super();
    }

    public ServerError(String errorMessage) {
        super(errorMessage);
    }
}