package result;

/**
 * Class that contains all information for a registerUser result.
 */
public class RegisterResult {

    private String authToken, userName, personID;

    /**
     * Constructor.
     */
    public RegisterResult(String authToken, String personID, String userName) {
        this.authToken = authToken;
        this.personID = personID;
        this.userName = userName;
    }

    public RegisterResult() {

    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}