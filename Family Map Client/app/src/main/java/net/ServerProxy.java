package net;

import model.Model;
import model.Event;
import model.Filter;
import model.Person;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import result.LoginResult;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import result.EventsResult;
import result.PersonsResult;
import com.google.gson.Gson;
import java.io.OutputStream;
import request.LoginRequest;
import result.RegisterResult;
import request.RegisterRequest;
import result.SinglePersonResult;
import java.net.HttpURLConnection;

public class ServerProxy {

    private static String getInput(InputStream inputStream) {

        StringBuilder stringBuilder = new StringBuilder();
        Scanner inputScanner = new Scanner(inputStream);

        while (inputScanner.hasNextLine()) {
            stringBuilder.append(inputScanner.nextLine());
        }

        inputScanner.close();
        return stringBuilder.toString();
    }

    private static void sendOutput(String outputString, OutputStream outputStream) {

        PrintWriter outputWriter = new PrintWriter(outputStream);
        outputWriter.print(outputString);
        outputWriter.close();
    }

    public RegisterResult registerUser(String serverHost, String serverPort, RegisterRequest registerRequest) {

        RegisterResult registerResult = new RegisterResult();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/registerUser");
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();

            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.addRequestProperty("Accept", "application/json");
            httpConnection.connect();

            Gson gson = new Gson();
            String requestData = gson.toJson(registerRequest);
            OutputStream requestBody = httpConnection.getOutputStream();

            sendOutput(requestData, requestBody);
            requestBody.close();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = httpConnection.getInputStream();
                String responseData = getInput(responseBody);

                registerResult = gson.fromJson(responseData, RegisterResult.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return registerResult;
    }

    public LoginResult loginUser(String serverHost, String serverPort, LoginRequest loginRequest) {

        LoginResult loginResult = new LoginResult();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/loginUser");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            Model model = Model.getModel();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            Gson gson = new Gson();
            String requestData = gson.toJson(loginRequest);
            OutputStream requestBody = http.getOutputStream();

            sendOutput(requestData, requestBody);
            requestBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = http.getInputStream();
                String responseData = getInput(responseBody);

                loginResult = gson.fromJson(responseData, LoginResult.class);
                model.setMainPersonID(loginResult.getPersonID());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return loginResult;
    }

    // This function was used for the login assignment, but isn't needed for the final app
    public Person getPerson(String serverHost, String serverPort) {

        Person foundPerson = null;

        Model model = Model.getModel();
        String personID = model.getMainPersonID();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/" + personID);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", model.getAuthToken());
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            Gson gson = new Gson();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = http.getInputStream();
                String responseData = getInput(responseBody);

                SinglePersonResult singlePerson = gson.fromJson(responseData, SinglePersonResult.class);
                foundPerson = singlePerson.getPerson();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return foundPerson;
    }

    public List<Person> getPersons(String serverHost, String serverPort) {

        List<Person> allPersons = new ArrayList<>();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            Model model = Model.getModel();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", model.getAuthToken());
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            Gson gson = new Gson();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = http.getInputStream();
                String responseData = getInput(responseBody);

                allPersons = gson.fromJson(responseData, PersonsResult.class).getPersons();
                TreeMap<String, Person> allPersonsMap = new TreeMap<>();

                for (Person person : allPersons) {
                    allPersonsMap.put(person.getPersonID(), person);
                }
                model.setAllPersons(allPersonsMap);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return allPersons;
    }

    public List<Event> getEvents(String serverHost, String serverPort) {

        List<Event> allEvents = new ArrayList<>();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            Model model = Model.getModel();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", model.getAuthToken());
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            Gson gson = new Gson();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = http.getInputStream();
                String responseData = getInput(responseBody);

                allEvents = gson.fromJson(responseData, EventsResult.class).getEvents();
                TreeMap<String, Event> allEventsMap = new TreeMap<>();

                for (Event event : allEvents) {
                    event.setType(event.getType().toLowerCase());
                    allEventsMap.put(event.getEventID(), event);
                    System.out.print(event.getEventID());
                }
                model.setAllEvents(allEventsMap);
                Filter filters = new Filter();
                filters.createFilters();
                filters.applyFilters();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return allEvents;
    }
}