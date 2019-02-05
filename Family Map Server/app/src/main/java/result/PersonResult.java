package result;

import model.Person;

/**
 * Class that contains all information for a person result.
 */
public class PersonResult {

    public String personID, descendant, firstName, lastName, gender,
            father, mother, spouse;

    public PersonResult(Person person) {
        personID = person.getPersonID();
        descendant = person.getDescendant();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        gender = person.getGender();
        father = person.getFather();
        mother = person.getMother();
        spouse = person.getSpouse();
    }
}