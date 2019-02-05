package model;

import result.EventResult;

/**
 * Class that contains all information for an event.
 */
public class Event {

    private String eventID, descendant, personID, latitude, longitude,
            country, city, eventType, year;

    public Event(String eventID, String descendant, String personID, String latitude,
                 String longitude, String country, String city, String eventType, String year) {

        this.eventID = eventID;
        this.descendant = descendant;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public Event(EventResult eventResult) {

        this.eventID = eventResult.eventID;
        this.descendant = eventResult.descendant;
        this.personID = eventResult.personID;
        this.latitude = eventResult.latitude;
        this.longitude = eventResult.longitude;
        this.country = eventResult.country;
        this.city = eventResult.city;
        this.eventType = eventResult.eventType;
        this.year = eventResult.year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return eventType;
    }

    public void setType(String eventType) {
        this.eventType = eventType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}