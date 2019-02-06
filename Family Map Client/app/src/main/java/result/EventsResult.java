package result;

import model.Event;
import java.util.List;
import java.util.ArrayList;

/**
 * Class that contains an array of all related family member events.
 */
public class EventsResult {

    private List<Event> data;

    /**
     * Constructor.
     */
    public EventsResult(List<Event> events) {
        data = new ArrayList<>();
        data = events;
    }

    public List<Event> getEvents() {
        return data;
    }

    public void setEvents(List<Event> events) {
        this.data = events;
    }
}