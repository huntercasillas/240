package result;

import model.Event;

/**
 * Class that contains all information for an event result.
 */
public class EventResult {

    public String eventID, descendant, personID, latitude, longitude, country, city, eventType, year;

    public EventResult(Event event) {
        eventID = event.getEventID();
        descendant = event.getDescendant();
        personID = event.getPersonID();
        latitude = event.getLatitude();
        longitude = event.getLongitude();
        country = event.getCountry();
        city = event.getCity();
        eventType = event.getType();
        year = event.getYear();
    }
}