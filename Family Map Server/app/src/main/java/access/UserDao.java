package access;

import model.User;
import java.util.UUID;
import error.ServerError;
import java.sql.ResultSet;
import java.sql.SQLException;
import request.RegisterRequest;
import java.sql.PreparedStatement;

/**
 * This class is for accessing users from the database.
 */
public class UserDao extends Database {

    private PreparedStatement statement;
    private ResultSet result = null;

    public UserDao() throws ServerError {

        String createTable = "create table if not exists user (" +
                "userName text not null primary key, password text not null, " +
                "email text not null, firstName text not null, " +
                "lastName text not null, gender text not null, personID text not null)";

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

    public String registerUser(RegisterRequest user) throws ServerError {

        String userGender = user.getGender().toLowerCase();
        String personID = UUID.randomUUID().toString();

        if (user.getUsername().length() <= 0) {
            throw new ServerError("Error. Username cannot be left empty.");
        } else if (user.getPassword().length() <= 0) {
            throw new ServerError("Error. Password cannot be left empty.");
        } else if (user.getEmail().length() <= 0) {
            throw new ServerError("Error. Email cannot be left empty.");
        } else if (user.getFirstName().length() <= 0) {
            throw new ServerError("Error. First Name cannot be left empty.");
        } else if (user.getLastName().length() <= 0) {
            throw new ServerError("Error. Last Name cannot be left empty.");
        } else if (!userGender.equals("m") && !userGender.equals("f")) {
            throw new ServerError("Error. Gender must be m or f.");
        }

        personID = personID.substring(0, 8);

        try {
            openConnection();
            String addUser = "insert into user values ( ?, ?, ?, ?, ?, ?, ? )";

            statement = connection.prepareStatement(addUser);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getGender());
            statement.setString(7, personID);
            statement.executeUpdate();
            statement.close();
            closeConnection();
        } catch (SQLException e) {
            throw new ServerError("Error. Could not register User. The username may already exist.");
        }
        return personID;
    }

    public boolean loginUser(String username, String password) throws ServerError {

        if (username == null || password == null) {
            return false;
        }

        String foundPassword;

        try {
            openConnection();
            String loginUser = "select * from user where userName = ? ";
            statement = connection.prepareStatement(loginUser);
            statement.setString(1, username);
            result = statement.executeQuery();

            try {
                foundPassword = result.getString("password");
            } catch (SQLException e) {
                return false;
            }

            result.close();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not login User.");
        }

        return foundPassword.equals(password);
    }

    public void addUser(User user) throws ServerError {

        String userGender = user.getGender().toLowerCase();

        if (user.getUsername().length() <= 0) {
            throw new ServerError("Error. Username cannot be left empty.");
        } else if (user.getPassword().length() <= 0) {
            throw new ServerError("Error. Password cannot be left empty.");
        } else if (user.getEmail().length() <= 0) {
            throw new ServerError("Error. Email cannot be left empty.");
        } else if (user.getFirstName().length() <= 0) {
            throw new ServerError("Error. First Name cannot be left empty.");
        } else if (user.getLastName().length() <= 0) {
            throw new ServerError("Error. Last Name cannot be left empty.");
        } else if (!userGender.equals("m") && !userGender.equals("f")) {
            throw new ServerError("Error. Gender must be m or f.");
        }

        try {
            openConnection();
            String addUser = "insert into user values ( ?, ?, ?, ?, ?, ?, ? )";

            statement = connection.prepareStatement(addUser);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getGender());
            statement.setString(7, user.getPersonID());
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not add User.");
        }
    }

    public User findUser(String username) throws ServerError {

        String name, password, email, firstName, lastName, gender, personID;
        User foundUser;

        if (username == null) {
            throw new ServerError("Error. Could not find User.");
        }

        try {
            openConnection();
            String findUser = "select * from user where userName = ? ";
            statement = connection.prepareStatement(findUser);
            statement.setString(1, username);
            result = statement.executeQuery();

            try {
                name = result.getString("userName");
                password = result.getString("password");
                email = result.getString("email");
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                gender = result.getString("gender");
                personID = result.getString("personID");
            } catch (SQLException e) {
                return null;
            }

            result.close();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not find User.");
        }

        foundUser = new User(name, password, email, firstName, lastName, gender, personID);
        return foundUser;
    }

    public void clearUsers() throws ServerError {

        try {
            openConnection();
            String clearUsers = "delete from user";
            statement = connection.prepareStatement(clearUsers);
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not clear Users.");
        }
    }
}