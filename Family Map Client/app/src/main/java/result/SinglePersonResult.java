package result;

import model.Person;

public class SinglePersonResult {

    private String personID, descendant, firstName, lastName, gender, father, mother;

    public SinglePersonResult(String personID, String descendant, String firstName, String lastName, String gender,
                  String father, String mother) {

        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public Person getPerson() {
        Person newPerson = new Person();
        newPerson.setPersonID(personID);
        newPerson.setDescendant(descendant);
        newPerson.setFirstName(firstName);
        newPerson.setLastName(lastName);
        newPerson.setGender(gender);
        newPerson.setFather(father);
        newPerson.setMother(mother);
        return newPerson;
    }
}