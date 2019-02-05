package access;

import model.Event;
import java.util.UUID;
import error.ServerError;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * This class is for accessing events from the database.
 */
public class EventDao extends Database {

    private PreparedStatement statement;
    private ResultSet result = null;

    public EventDao() throws ServerError {

        String createTable = "create table if not exists event (" +
                "eventID text not null primary key, descendant text not null, " +
                "person text not null, latitude text not null, " +
                "longitude text not null, country text not null, " +
                "city text not null, eventType text not null, year text not null)";

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

    public void addEvent(Event event) throws ServerError {

        String eventID = UUID.randomUUID().toString();
        eventID = eventID.substring(0, 8);

        try {
            openConnection();
            String addEvent = "insert into event values ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

            if (event.getEventID() == null) {
                event.setEventID(eventID);
            }
            statement = connection.prepareStatement(addEvent);
            statement.setString(1, event.getEventID());
            statement.setString(2, event.getDescendant());
            statement.setString(3, event.getPersonID());
            statement.setString(4, event.getLatitude());
            statement.setString(5, event.getLongitude());
            statement.setString(6, event.getCountry());
            statement.setString(7, event.getCity());
            statement.setString(8, event.getType());
            statement.setString(9, event.getYear());
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not add Event.");
        }
    }

    public void removeDescendantEvents(String username) throws ServerError {

        try {
            openConnection();
            String clearDescendantEvents = "delete from event where descendant = ? ";
            statement = connection.prepareStatement(clearDescendantEvents);
            statement.setString(1, username);
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not clear Descendant Events.");
        }
    }

    public Event findEvent(String eventID) throws ServerError {

        String ID, descendant, person, latitude, longitude, country, city, type, year;
        Event foundEvent;

        if (eventID == null) {
            throw new ServerError("Error. Could not find Event.");
        }

        try {
            openConnection();
            String findEvent = "select * from event where eventID = ? ";
            statement = connection.prepareStatement(findEvent);
            statement.setString(1, eventID);
            result = statement.executeQuery();

            try {
                ID = result.getString("eventID");
                descendant = result.getString("descendant");
                person = result.getString("person");
                country = result.getString("country");
                city = result.getString("city");
                type = result.getString("eventType");
                year = result.getString("year");
                latitude = result.getString("latitude");
                longitude = result.getString("longitude");
            } catch (SQLException e) {
                throw new ServerError("Error. Could not find Event.");
            }

            result.close();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not find Event.");
        }

        foundEvent = new Event(ID, descendant, person, latitude, longitude, country, city, type, year);
        return foundEvent;
    }

    public ArrayList<Event> findAllEvents(String username) throws ServerError {

        String ID, descendant, person, latitude, longitude, country, city, type, year;
        ArrayList<Event> allEvents = new ArrayList<>();

        if (username == null) {
            return null;
        }

        try {
            openConnection();
            String findAllEvents = "select * from event where descendant = ? ";
            statement = connection.prepareStatement(findAllEvents);
            statement.setString(1, username);
            result = statement.executeQuery();

            try {

                while (result.next()) {
                    ID = result.getString("eventID");
                    descendant = result.getString("descendant");
                    person = result.getString("person");
                    country = result.getString("country");
                    city = result.getString("city");
                    type = result.getString("eventType");
                    year = result.getString("year");
                    latitude = result.getString("latitude");
                    longitude = result.getString("longitude");
                    Event currentEvent = new Event(ID, descendant, person, latitude, longitude, country, city, type, year);
                    allEvents.add(currentEvent);
                }
            } catch (SQLException e) {
                return null;
            }

            result.close();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not find Events.");
        }

        if (allEvents.size() == 0) {
            return null;
        } else {
            return allEvents;
        }
    }

    public void clearEvents() throws ServerError {

        try {
            openConnection();
            String clearEvents = "delete from event";
            statement = connection.prepareStatement(clearEvents);
            statement.executeUpdate();
            statement.close();
            closeConnection();

        } catch (SQLException e) {
            throw new ServerError("Error. Could not clear Events.");
        }
    }
}