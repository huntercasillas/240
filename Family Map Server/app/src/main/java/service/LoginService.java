package service;

import model.User;
import access.UserDao;
import error.ServerError;
import result.LoginResult;
import access.AuthTokenDao;
import request.LoginRequest;

/**
 * Class that contains all information for logging a user into the database.
 */
public class LoginService {

    private AuthTokenDao authDAO;
    private UserDao userDAO;

    public LoginService() throws ServerError {
        try {
            authDAO = new AuthTokenDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    public LoginResult loginUser(LoginRequest loginRequest) throws ServerError {

        User loginUser;
        String authToken;

        try {
            if (!userDAO.loginUser(loginRequest.getUsername(), loginRequest.getPassword())) {
                throw new ServerError("Error. Username and password do not match.");
            }
            loginUser = userDAO.findUser(loginRequest.getUsername());
            authToken = authDAO.addAuthToken(loginRequest.getUsername());
        }
        catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }

        return new LoginResult(authToken, loginUser.getUsername(), loginUser.getPersonID());
    }
}