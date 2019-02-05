package request;

/**
 * Class that contains all information for a login request.
 */
public class LoginRequest {

    private String userName, password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}