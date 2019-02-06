package result;

import model.Person;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains an array of all related family member persons.
 */
public class PersonsResult {

    private List<Person> data;

    /**
     * Constructor.
     */
    public PersonsResult(List<Person> persons) {
        data = new ArrayList<>();
        data = persons;
    }

    public List<Person> getPersons() {
        return data;
    }

    public void setPersons(List<Person> persons) {
        this.data = persons;
    }
}