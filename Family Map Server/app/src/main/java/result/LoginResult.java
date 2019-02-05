package result;

/**
 * Class that contains all information for a login result.
 */
public class LoginResult {

    public String authToken, username, personID;
    
    public LoginResult(String authToken, String username, String personID) {
        this.authToken = authToken;
        this.username = username;
        this.personID = personID;
    }
}