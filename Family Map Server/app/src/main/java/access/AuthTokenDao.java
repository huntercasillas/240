package access;

import model.AuthToken;
import error.ServerError;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * This class is for accessing authorization tokens from the database.
 */
public class AuthTokenDao extends Database {

    private PreparedStatement statement;
    private ResultSet result;

    public AuthTokenDao() throws ServerError {

        String createTable = "create table if not exists authToken (authToken " +
                "text not null primary key, userName text not null)";
        result = null;

        try {
            openConnection();
            statement = connection.prepareStatement(createTable);
            statement.executeUpdate();
            statement.close();
            closeConnection();
        }
        catch (SQLException e) {
            throw new ServerError("Error. Could not create table in database.");
        }
    }

    public String addAuthToken(String username) throws ServerError {

        AuthToken authToken = new AuthToken(username);

        if (username.length() <= 0) {
            throw new ServerError("Error. Could not add AuthToken.");
        }

        try {
            openConnection();
            String addAuthToken = "insert into authToken values ( ?, ? )";
            statement = connection.prepareStatement(addAuthToken);
            statement.setString(1, authToken.getToken());
            statement.setString(2, authToken.getUsername());
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not add AuthToken.");
        }

        return authToken.getToken();
    }

    public String findUsername(String authToken) throws ServerError {

        String username;

        if (authToken == null || authToken.length() <= 0) {
            throw new ServerError("Error. Invalid Authorization Token.");
        } else {
            try {
                openConnection();
                String findAuthToken = "select * from authToken where authToken = ? ";
                statement = connection.prepareStatement(findAuthToken);
                statement.setString(1, authToken);
                result = statement.executeQuery();
                username = result.getString("username");
                statement.close();
                result.close();
                closeConnection();

            } catch (SQLException e) {
                throw new ServerError("Error. Could not find Username.");
            }
        }

        return username;
    }

    public void clearAuthTokens() throws ServerError {

        try {
            openConnection();
            String clearAuthTokens = "delete from authToken";
            statement = connection.prepareStatement(clearAuthTokens);
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not clear AuthTokens.");
        }
    }
}