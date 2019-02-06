package tests;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import model.Model;
import model.Event;
import model.Person;
import model.Search;
import model.Filter;
import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

/**
 * Automated Tests - for each method, you should test both success and failure cases.
 * Write JUnit tests to verify that your model is correctly:
 * Calculating family relationships (i.e., spouses, parents, children).
 * Filtering events according to the current filter settings.
 * Chronologically sorting a person’s individual events (birth first, death last, etc).
 * Searching for people and events.
 */
public class ModelTest {

    private Model model;
    private TreeMap<String, Person> testPersons;
    private TreeMap<String, Event> testEvents;

    @Before
    public void setDefault() {

        model = Model.getModel();

        testPersons = new TreeMap<>();
        testEvents = new TreeMap<>();

        Person person1 = new Person("id1p", "id1p", "Hunter", "Casillas",
                "m", "id3p", "id2p", "id5p");

        Person person2 = new Person("id2p", "id1p", "Sierra", "Driggs",
                "f", null, null, "id3p");

        Person person3 = new Person("id3p", "id1p", "Drew", "Hafer",
                "m", null, null, "id2p");

        Person person4 = new Person("id4p", "id1p", "Sean", "Moran",
                "m", null, null, null);

        Person person5 = new Person("id5p", "id1p", "Paige", "Timberlake",
                "f", null, null, null);


        Event event1 = new Event("id1e", "id1p", "id1p", "111", "111",
                "United States", "Arlington", "birth", "1994");

        Event event2 = new Event("id2e", "id1p", "id2p", "222", "222",
                "United States", "Provo", "death", "2075");

        Event event3 = new Event("id3e", "id1p", "id3p", "333", "333",
                "France", "Paris", "marriage", "2025");

        Event event4 = new Event("id4e", "id1p", "id4p", "444", "444",
                "United States", "Philadelphia", "baptism", "2002");

        Event event5 = new Event("id5e", "id1p", "id5p", "555", "555",
                "United States", "Ashburn", "graduation", "2013");

        Event event6 = new Event("id6e", "id1p", "id1p", "665", "665",
                "United States", "Orem", "death", "2016");

        testPersons.put(person1.getPersonID(), person1);
        testPersons.put(person2.getPersonID(), person2);
        testPersons.put(person3.getPersonID(), person3);
        testPersons.put(person4.getPersonID(), person4);
        testPersons.put(person5.getPersonID(), person5);

        testEvents.put(event1.getType(), event1);
        testEvents.put(event2.getType(), event2);
        testEvents.put(event3.getType(), event3);
        testEvents.put(event4.getType(), event4);
        testEvents.put(event5.getType(), event5);
        testEvents.put(event6.getType(), event6);

        model.setAllPersons(testPersons);
        model.setAllEvents(testEvents);
    }

    @After
    public void resetDefault() {
        testPersons.clear();
        testEvents.clear();
    }

    @Test
    public void searchSinglePersonTest() {
        String searchRequest = "hunter casillas";
        Search testSearch = new Search(searchRequest);

        // Should only return hunter casillas
        assertEquals(1, testSearch.getFoundPersons().size());
        assertEquals("Hunter", testSearch.getFoundPersons().get(0).getFirstName());
    }

    @Test
    public void searchPersonsTest() {
        String searchRequest = "s";
        Search testSearch = new Search(searchRequest);

        // Should return hunter casillas, sierra driggs, and sean moran
        assertEquals(3, testSearch.getFoundPersons().size());
    }

    @Test
    public void searchSingleEventTest() {
        String searchRequest = "philadelphia";
        Search testSearch = new Search(searchRequest);

        // Should only return 1 event of type baptism in city Philadelphia
        assertEquals(1, testSearch.getFoundEvents().size());
    }

    @Test
    public void searchEventsTest() {
        String searchRequest = "P";
        Search testSearch = new Search(searchRequest);

        // Should return provo, philadelphia (baptism), and paris
        assertEquals(3, testSearch.getFoundEvents().size());
    }

    @Test
    public void getSinglePersonTest() {
        Person testPerson = model.getPerson("id3p");

        // The first name of the personID we use should be Drew
        assertEquals(testPerson.getFirstName(), "Drew");
    }

    @Test
    public void getSingleEventTest() {
        Event testEvent = model.getEvent("id3e");

        // The associated personID from the given event should be id3p
        assertEquals(testEvent.getPersonID(), "id3p");
    }

    @Test
    public void getEventsTest() {
        int eventsSize = model.getAllEvents().size();

        // We added 6 events in the default set up, so the eventsSize should be 6
        assertEquals(6, eventsSize);
    }

    @Test
    public void getGenderTest() {
        String testPersonID = ("id1p");
        String gender = model.getGender(testPersonID);

        // Gender of Hunter Casillas should be male
        assertEquals(gender, "m");
    }

    @Test
    public void getFatherTest() {
        Person testPerson = model.getPerson("id1p");
        String testFatherID = testPerson.getFather();

        // Father should be Drew Hafer, whose personID is id3p
        assertEquals("id3p", testFatherID);
    }

    @Test
    public void getMotherTest() {
        Person testPerson = model.getPerson("id1p");
        String testMotherID = testPerson.getMother();

        // Mother should be Sierra Driggs, whose personID is id2p
        assertEquals("id2p", testMotherID);
    }

    @Test
    public void getChildTest() {
        Person testPerson = model.getPerson("id2p");
        String testChildID = testPerson.getDescendant();

        // Child should be Hunter Casillas, whose personID is id1p
        assertEquals("id1p", testChildID);
    }

    @Test
    public void getSpouseTest() {
        Person testPerson = model.getPerson("id1p");
        String testSpouseID = testPerson.getSpouse();

        // Spouse should be Paige Timberlake, whose personID is id5p
        assertEquals("id5p", testSpouseID);
    }

    @Test
    public void getSortedEventsTest() {
        TreeMap<String, Event> allEvents = model.getAllEvents();
        List<Event> sortedEvents = new ArrayList<>();
        Event firstEvent;
        Event secondEvent;

        for (Event event : allEvents.values()) {
            if (event.getPersonID().equals("idp1")) {
                sortedEvents.add(event);
            }
        }

        firstEvent = sortedEvents.get(0);
        secondEvent = sortedEvents.get(1);

        if (Integer.parseInt(firstEvent.getYear()) > Integer.parseInt(secondEvent.getYear())) {
            fail();
        }
    }

    @Test
    public void filterEventsTest() {
        int totalEventsSize = model.getAllEvents().size();
        Filter filters = new Filter();
        filters.createFilters();
        filters.applyFilters();

        List<Filter> filterList = new ArrayList<>(model.getFilters().values());
        Filter currentFilter = filterList.get(0);

        model.getFilters().get(currentFilter.getFilterType()).setFilterStatus(false);
        filters.createFilters();
        filters.applyFilters();

        int filteredEventsSize = model.getFilteredEvents().size();

        if (filteredEventsSize >= totalEventsSize) {
            fail();
        }
    }
}