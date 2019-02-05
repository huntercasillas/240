package access;

import model.Person;
import java.util.UUID;
import error.ServerError;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * This class is for accessing people from the database.
 */
public class PersonDao extends Database {

    private PreparedStatement statement;
    private ResultSet result = null;

    public PersonDao() throws ServerError {

        String createTable = "create table if not exists person (" +
                "personID text primary key, descendant text not null, " +
                "firstName text not null, lastName text not null, " +
                "gender text not null, father text, " +
                "mother text, spouse text)";

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

    public void addPerson(Person person) throws ServerError {

        String personID = UUID.randomUUID().toString();
        personID = personID.substring(0, 8);

        try {
            openConnection();
            String addPerson = "insert into person values ( ?, ?, ?, ?, ?, ?, ?, ? )";

            if (person.getPersonID() == null) {
                person.setPersonID(personID);
            }
            statement = connection.prepareStatement(addPerson);
            statement.setString(1, person.getPersonID());
            statement.setString(2, person.getDescendant());
            statement.setString(3, person.getFirstName());
            statement.setString(4, person.getLastName());
            statement.setString(5, person.getGender());
            statement.setString(6, person.getFather());
            statement.setString(7, person.getMother());
            statement.setString(8, person.getSpouse());
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not add Person.");
        }
    }

    public void removeDescendants(String username) throws ServerError {

        try {
            openConnection();
            String clearDescendants = "delete from person where descendant = ? ";
            statement = connection.prepareStatement(clearDescendants);
            statement.setString(1, username);
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not clear Descendants.");
        }
    }

    public Person findPerson(String personID) throws ServerError {

        String ID, descendant, firstName, lastName, gender, father, mother, spouse;
        Person foundPerson;

        if (personID == null) {
            throw new ServerError("Error. Could not find Person.");
        }

        try {
            openConnection();
            String findPerson = "select * from person where personID = ? ";
            statement = connection.prepareStatement(findPerson);
            statement.setString(1, personID);
            result = statement.executeQuery();

            try {
                ID = result.getString("personID");
                descendant = result.getString("descendant");
                firstName = result.getString("firstName");
                lastName = result.getString("lastName");
                gender = result.getString("gender");
                father = result.getString("father");
                mother = result.getString("mother");
                spouse = result.getString("spouse");
            } catch (SQLException e) {
                throw new ServerError("Error. Could not find Person.");
            }

            result.close();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not find Person.");
        }

        foundPerson = new Person(ID, descendant, firstName, lastName, gender, father, mother, spouse);
        return foundPerson;
    }

    public ArrayList<Person> findAllPersons(String username) throws ServerError {

        String ID, descendant, firstName, lastName, gender, father, mother, spouse;
        ArrayList<Person> allPersons = new ArrayList<>();

        if (username == null) {
            return null;
        }

        try {
            openConnection();
            String findAllPersons = "select * from person where descendant = ? ";
            statement = connection.prepareStatement(findAllPersons);
            statement.setString(1, username);
            result = statement.executeQuery();

            try {

                while (result.next()) {
                    ID = result.getString("personID");
                    descendant = result.getString("descendant");
                    firstName = result.getString("firstName");
                    lastName = result.getString("lastName");
                    gender = result.getString("gender");
                    father = result.getString("father");
                    mother = result.getString("mother");
                    spouse = result.getString("spouse");
                    Person currentPerson = new Person(ID, descendant, firstName, lastName, gender, father, mother, spouse);
                    allPersons.add(currentPerson);
                }
            } catch (SQLException e) {
                return null;
            }

            result.close();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not find Persons.");
        }

        if (allPersons.size() == 0) {
            return null;
        } else {
            return allPersons;
        }

    }

    public void clearPersons() throws ServerError {

        try {
            openConnection();
            String clearPersons = "delete from person";
            statement = connection.prepareStatement(clearPersons);
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not clear Persons.");
        }
    }
}