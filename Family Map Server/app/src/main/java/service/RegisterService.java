package service;

import access.UserDao;
import error.ServerError;
import result.LoginResult;
import access.AuthTokenDao;
import request.RegisterRequest;

/**
 * Class that contains all information for registering a user to the database.
 */
public class RegisterService {

    private AuthTokenDao authDAO;
    private UserDao userDAO;

    public RegisterService() throws ServerError {

        try {
            authDAO = new AuthTokenDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    public LoginResult registerUser(RegisterRequest registerRequest) throws ServerError {

        String userID;
        String authToken;
        FillService fillService = new FillService();

        try {
            userID = userDAO.registerUser(registerRequest);
            authToken = authDAO.addAuthToken(registerRequest.getUsername());
        }
        catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }

        fillService.fillInformation(registerRequest.getUsername(), 4);
        return new LoginResult(authToken, registerRequest.getUsername(), userID);
    }
}