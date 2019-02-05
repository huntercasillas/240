package result;

import model.Person;
import java.util.List;
import java.util.ArrayList;

/**
 * Class that contains all information for a persons result.
 */
public class PersonsResult {

    List<Person> data;

    public PersonsResult(List<Person> persons) {
        data = new ArrayList<>();
        for (Person person : persons) {
            data.add(person);
        }
    }
}