package service;

import model.*;
import server.Server;
import utility.Data;
import java.util.UUID;
import access.UserDao;
import access.EventDao;
import java.util.Random;
import access.PersonDao;
import error.ServerError;
import java.util.ArrayList;
import result.MessageResult;

/**
 * Class that contains all information for filling the database.
 */
public class FillService {

    private ArrayList<Person> allPersons;
    private ArrayList<Event> allEvents;
    private PersonDao personDAO;
    private EventDao eventDAO;
    private UserDao userDAO;
    private User rootUser;
    private Data locations;
    private Data maleNames;
    private Data femaleNames;
    private Data lastNames;

    public FillService() throws ServerError {
        try {
            eventDAO = new EventDao();
            personDAO = new PersonDao();
            userDAO = new UserDao();
        } catch (ServerError e) {
            throw new ServerError(e.getMessage());
        }
    }

    public MessageResult fillInformation(String username, int generations) {

        String serverResponse;

        if (generations == 0) {
            generations = 4;
        }

        try {
            if (generations <= 0) {
                throw new ServerError("Error. Please enter a valid generation number.");
            }

            locations = new Data();
            maleNames = new Data();
            femaleNames = new Data();
            lastNames = new Data();

            allPersons = new ArrayList<>();
            allEvents = new ArrayList<>();
            rootUser = userDAO.findUser(username);

            String rootFatherID = UUID.randomUUID().toString();
            rootFatherID = rootFatherID.substring(0, 8);

            String rootMotherID = UUID.randomUUID().toString();
            rootMotherID = rootMotherID.substring(0, 8);

            personDAO.removeDescendants(username);
            eventDAO.removeDescendantEvents(username);

            Person rootPerson = new Person(rootUser.getPersonID(), username, rootUser.getFirstName(),
                    rootUser.getLastName(), rootUser.getGender(), rootFatherID, rootMotherID, null);

            allPersons.add(rootPerson);

            fillPersons(username, generations);
            fillEvents(username);

            for (Person person : allPersons) {
                personDAO.addPerson(person);
            }

            for (Event event : allEvents) {
                eventDAO.addEvent(event);
            }

            serverResponse = "Successfully added " + allPersons.size() + " persons and "
                    + allEvents.size() + " events to the database.";

        } catch (ServerError e) {
            serverResponse = e.getMessage();
        }

        return new MessageResult(serverResponse);
    }

    private void fillPersons(String username, int generations) {

        int totalPersons = 3;

        if (generations >= 2) {
            for (int i = 1; i < generations; i++) {
                totalPersons = (totalPersons * 2) + 1;
            }
        }

        for (int i = 1; i < (totalPersons / 2) + 1; i++) {

            String fatherID = UUID.randomUUID().toString();
            fatherID = fatherID.substring(0, 8);
            String fatherFirstName = maleNames.getRandomMaleName();
            String fatherLastName = rootUser.getLastName();

            String motherID = UUID.randomUUID().toString();
            motherID = motherID.substring(0, 8);
            String motherFirstName = femaleNames.getRandomFemaleName();
            String motherLastName = lastNames.getRandomLastName();

            Person father = new Person(fatherID, username, fatherFirstName, fatherLastName,
                    "m", null, null, motherID);
            Person mother = new Person(motherID, username, motherFirstName, motherLastName,
                    "f", null, null, fatherID);

            allPersons.add(father);
            allPersons.add(mother);
        }

        for (int i = 0, index = 1; index < allPersons.size(); i++, index += 2) {

            allPersons.get(i).setFather(allPersons.get(index).getPersonID());
            allPersons.get(i).setMother(allPersons.get(index + 1).getPersonID());
        }
    }

    private void fillEvents(String username) throws ServerError {

        int randomOffset;
        int randomIncrease = 5;
        int randomDecrease = 4;
        Random random = new Random();

        int baptismAge = 8;
        int graduationAge = 25;
        int marriageAge = 35;
        int deathAge = 80;
        int generationAge = 30;

        int currentYear = 2018;
        int generationGap = 30;
        int generationLoop = 0;

        for (int i = 0; i < allPersons.size(); i++) {

            randomOffset = random.nextInt(randomIncrease) - randomDecrease;
            int birthYear = (currentYear - generationGap) + randomOffset;

            randomOffset = random.nextInt(randomIncrease) - randomDecrease;
            int baptismYear = (birthYear + baptismAge) + randomOffset;

            randomOffset = random.nextInt(randomIncrease) - randomDecrease;
            int graduationYear = (birthYear + graduationAge) + randomOffset;

            randomOffset = random.nextInt(randomIncrease) - randomDecrease;
            int deathYear = (birthYear + deathAge) + randomOffset;

            String eventID1 = UUID.randomUUID().toString().substring(0, 8);
            String eventID2 = UUID.randomUUID().toString().substring(0, 8);
            String eventID3 = UUID.randomUUID().toString().substring(0, 8);
            String eventID4 = UUID.randomUUID().toString().substring(0, 8);

            Location location1 = locations.getRandomLocation();
            Location location2 = locations.getRandomLocation();
            Location location3 = locations.getRandomLocation();
            Location location4 = locations.getRandomLocation();

            if (birthYear < currentYear) {
                allEvents.add(new Event(eventID1, username, allPersons.get(i).getPersonID(),
                        location1.getLatitude(), location1.getLongitude(), location1.getCountry(),
                        location1.getCity(), "Birth", Integer.toString(birthYear)));
            }

            if (baptismYear < currentYear) {
                allEvents.add(new Event(eventID2, username, allPersons.get(i).getPersonID(),
                        location2.getLatitude(), location2.getLongitude(), location2.getCountry(),
                        location2.getCity(), "Baptism", Integer.toString(baptismYear)));
            }

            if (graduationYear < currentYear) {
                allEvents.add(new Event(eventID3, username, allPersons.get(i).getPersonID(),
                        location3.getLatitude(), location3.getLongitude(), location3.getCountry(),
                        location3.getCity(), "Graduation", Integer.toString(graduationYear)));
            }

            if (deathYear < currentYear) {
                allEvents.add(new Event(eventID4, username, allPersons.get(i).getPersonID(),
                        location4.getLatitude(), location4.getLongitude(), location4.getCountry(),
                        location4.getCity(), "Death", Integer.toString(deathYear)));
            }

            if (i == 0) {
                generationGap += generationAge;
            }

            if (generationLoop == 2 || generationLoop == 6 || generationLoop  == 14 || generationLoop == 30
                    || generationLoop == 62 || generationLoop == 126 || generationLoop == 254 || generationLoop == 510
                    || generationLoop == 1022 || generationLoop == 2046) {
                generationGap += generationAge;
                System.out.println("");
            }

            generationLoop++;
        }

        for (int i = 1; i < allPersons.size(); i += 2) {

            int birthYear;
            int marriageYear;
            randomOffset = random.nextInt(randomIncrease) - randomDecrease;

            String currentPersonID = allPersons.get(i).getPersonID();
            Event birthEvent = new Event("random", "random", "random", "random",
                    "random", "random", "random", "random", "random");

            for (Event event : allEvents) {
                if (event.getPersonID().equals(currentPersonID)) {
                    if (event.getType().equals("Birth")) {
                        birthEvent = event;
                    }
                }
            }

            if (!birthEvent.getYear().equals("random")) {
                birthYear = Integer.parseInt(birthEvent.getYear()) + randomOffset;
                marriageYear = (birthYear + marriageAge) + randomOffset;
            } else {
                throw new ServerError("Error. Event year is invalid.");
            }

            String eventID5 = UUID.randomUUID().toString().substring(0, 8);
            String eventID6 = UUID.randomUUID().toString().substring(0, 8);

            Location marriageLocation = locations.getRandomLocation();

            if (marriageYear < currentYear) {
                allEvents.add(new Event(eventID5, username, allPersons.get(i).getPersonID(),
                        marriageLocation.getLatitude(), marriageLocation.getLongitude(), marriageLocation.getCountry(),
                        marriageLocation.getCity(), "Marriage", Integer.toString(marriageYear)));
                allEvents.add(new Event(eventID6, username, allPersons.get(i + 1).getPersonID(),
                        marriageLocation.getLatitude(), marriageLocation.getLongitude(), marriageLocation.getCountry(),
                        marriageLocation.getCity(), "Marriage", Integer.toString(marriageYear)));
            }
        }
    }
}