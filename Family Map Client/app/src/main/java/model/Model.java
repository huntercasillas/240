package model;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import result.LoginResult;
import java.util.ArrayList;
import result.RegisterResult;
import com.google.android.gms.maps.model.Marker;

/**
 * Container for model that holds all information necessary.
 */
public class Model {

    private static Model model;
    private LoginResult loginResult;
    private RegisterResult registerResult;
    private String authToken, mainPersonID, serverHost, serverPort;
    private Person mainPerson;
    private Event currentEvent;
    private TreeMap<String, Person> allPersons;
    private TreeMap<String, Event> allEvents;
    private TreeMap<String, String> eventColors = new TreeMap<>();
    private HashMap<Marker, Event> eventMapMarker = new HashMap<>();
    private TreeMap<String, Person> motherSideList = new TreeMap<>();
    private TreeMap<String, Person> fatherSideList = new TreeMap<>();
    private TreeMap<String, Filter> filters = new TreeMap<>();
    private HashSet<Event> filteredEvents = new HashSet<>();
    private Settings settings = new Settings();
    private boolean settingsStatus = false;

    /**
     * Empty Constructor.
     */
    private Model() {

    }

    public static Model getModel() {
        if (model != null) {
            return model;
        } else {
            model = new Model();
            return model;
        }
    }

    public LoginResult getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    public RegisterResult getRegisterResult() {
        return registerResult;
    }

    public void setRegisterResult(RegisterResult registerResult) {
        this.registerResult = registerResult;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMainPersonID() {
        return mainPersonID;
    }

    public void setMainPersonID(String mainPersonID) {
        this.mainPersonID = mainPersonID;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public boolean getSettingsStatus() {
        return settingsStatus;
    }

    public void setSettingsStatus(boolean settingsStatus) {
        this.settingsStatus = settingsStatus;
    }

    public Person getPerson(String personID) {
        try {
            return allPersons.get(personID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TreeMap<String, Person> getAllPersons() {
        return allPersons;
    }

    public void setAllPersons(TreeMap<String, Person> allPersons) {
        this.allPersons = allPersons;
    }

    Person getMainPerson() {
        try {
            mainPerson = allPersons.get(mainPersonID);
            return mainPerson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setMainPerson(Person person) {
        this.mainPerson = person;
    }

    public Event getEvent(String eventID) {
        try {
            return allEvents.get(eventID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public TreeMap<String, Event> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(TreeMap<String, Event> allEvents) {
        this.allEvents = allEvents;
        setEventColors();
    }

    public TreeMap<String, String> getEventColors() {
        return eventColors;
    }

    private void setEventColors() {
        List<String> eventTypes = new ArrayList<>();
        List<String> eventColors = new ArrayList<>();
        eventColors.add("Red");
        eventColors.add("Orange");
        eventColors.add("Yellow");
        eventColors.add("Green");
        eventColors.add("Blue");
        eventColors.add("Violet");
        eventColors.add("Rose");
        eventColors.add("Azure");
        eventColors.add("Magenta");
        eventColors.add("Cyan");

        for (Event event : model.getAllEvents().values()) {
            if (!eventTypes.contains(event.getType())) {
                eventTypes.add(event.getType());
            }
        }

        int i = 0;

        for (String eventType : eventTypes) {
            this.eventColors.put(eventType, eventColors.get(i));
            i++;
        }
    }

    public HashMap<Marker, Event> getEventMapMarker() {
        return eventMapMarker;
    }

    void setFatherSideList(TreeMap<String, Person> fatherSideList) {
        this.fatherSideList = fatherSideList;
    }

    public TreeMap<String, Person> getFatherSideList() {
        return fatherSideList;
    }

    void setMotherSideList(TreeMap<String, Person> motherSideList) {
        this.motherSideList = motherSideList;
    }

    public TreeMap<String, Person> getMotherSideList() {
        return motherSideList;
    }

    public TreeMap<String, Filter> getFilters() {
        return filters;
    }

    void setFilters(TreeMap<String, Filter> filters) {
        this.filters = filters;
    }

    void setFilteredEvents(HashSet<Event> filteredEvents) {
        this.filteredEvents = filteredEvents;
    }

    public HashSet<Event> getFilteredEvents() {
        return filteredEvents;
    }

    public String getGender(String personID) {
        return allPersons.get(personID).getGender();
    }
}