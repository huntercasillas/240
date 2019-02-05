package result;

import model.Event;
import java.util.List;
import java.util.ArrayList;

/**
 * Class that contains all information for an events result.
 */
public class EventsResult {

    public List<EventResult> data;

    public EventsResult(List<Event> events) {
        data = new ArrayList<>();
        for (Event event : events){
            data.add(new EventResult(event));
        }
    }
}