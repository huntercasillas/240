package access;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Database {

    Connection connection = null;

    public Database() {

        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Error. Could not find the SQLite driver.");
        }
    }

    void openConnection() {

        connection = null;
        String databasePath = "jdbc:sqlite:familyData.db";

        try {
            connection = DriverManager.getConnection(databasePath);
        }
        catch (SQLException e) {
            System.out.println("Error. Database connection could not be established.");
        }
    }

    void closeConnection() {

        try {
            connection.close();

        } catch (SQLException e) {
            System.out.println("Error. Database connection could not be closed.");
        }
    }
}