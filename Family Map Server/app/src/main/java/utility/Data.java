package utility;

import model.Location;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import java.io.FileNotFoundException;

/**
 * Class that contains all information for json names and locations.
 */
public class Data {

    private LocationData locations;
    private MaleNames maleNames;
    private FemaleNames femaleNames;
    private LastNames lastNames;

    public Data() {

        Gson gson = new Gson();

        try {
            String locationPath = "data/json/locations.json";
            String maleNamesPath = "data/json/mnames.json";
            String femaleNamesPath = "data/json/fnames.json";
            String lastNamesPath = "data/json/snames.json";

            locations = gson.fromJson(new String(Files.readAllBytes(Paths.get(locationPath))), LocationData.class);
            maleNames = gson.fromJson(new String(Files.readAllBytes(Paths.get(maleNamesPath))), MaleNames.class);
            femaleNames = gson.fromJson(new String(Files.readAllBytes(Paths.get(femaleNamesPath))), FemaleNames.class);
            lastNames = gson.fromJson(new String(Files.readAllBytes(Paths.get(lastNamesPath))), LastNames.class);

        } catch (FileNotFoundException e) {
            System.out.println("Error. Could not find JSON file.");
        } catch (IOException e) {
            System.out.println("Error occurred when converting to GSON.");
        }
    }

    public MaleNames getMaleNames() {
        return maleNames;
    }

    public String getRandomMaleName() {
        Random randomName = new Random();
        int index = randomName.nextInt(maleNames.data.length);
        return maleNames.data[index];
    }

    public FemaleNames getFemaleNames() {
        return femaleNames;
    }

    public String getRandomFemaleName() {
        Random randomName = new Random();
        int index = randomName.nextInt(femaleNames.data.length);
        return femaleNames.data[index];
    }

    public LastNames getLastNames() {
        return lastNames;
    }

    public String getRandomLastName() {
        Random randomName = new Random();
        int index = randomName.nextInt(lastNames.data.length);
        return lastNames.data[index];
    }

    public LocationData getLocations() {
        return locations;
    }

    public Location getRandomLocation() {
        Random randomLocation = new Random();
        int index = randomLocation.nextInt(locations.data.length);
        return locations.data[index];
    }
}