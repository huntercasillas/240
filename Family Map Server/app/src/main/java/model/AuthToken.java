package model;

import java.util.UUID;

/**
 * Class that contains all information for an authorization token.
 */
public class AuthToken {

    private String token;
    private String username;

    public AuthToken(String username) {

        String tokenID = UUID.randomUUID().toString();
        tokenID = tokenID.substring(0,8);

        this.token = tokenID;
        this.username = username;
    }

    public AuthToken(String username, String token) {

        this.token = token;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}