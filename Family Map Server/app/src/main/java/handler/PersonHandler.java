package handler;

import java.net.URI;
import error.ServerError;
import java.io.IOException;
import result.PersonsResult;
import result.PersonResult;
import result.MessageResult;
import service.PersonService;
import service.PersonsService;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import static java.net.HttpURLConnection.HTTP_OK;

public class PersonHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new Gson();
        MessageResult errorMessage = new MessageResult("Internal Server Error.");
        String serverResponse = gson.toJson(errorMessage);

        PersonResult personResult = null;
        boolean personSuccess = false;

        PersonsResult personsResult = null;
        boolean personsSuccess = false;

        String authToken = exchange.getRequestHeaders().getFirst("Authorization");
        URI requestURI = exchange.getRequestURI();
        String[] splitURI = requestURI.getPath().split("/");

        if (splitURI.length > 3) {
            errorMessage = new MessageResult("Error. There are too many parameters.");
            exchange.sendResponseHeaders(HTTP_OK, 0);
            sendResponseBody(exchange, gson.toJson(errorMessage));
            return;
        } else if (splitURI.length == 3) {
            try {
                personSuccess = true;
                PersonService personService = new PersonService();
                personResult = personService.findPerson(authToken, splitURI[2]);
            } catch (ServerError e) {
                errorMessage = new MessageResult("Error. The requested person does not exist.");
                exchange.sendResponseHeaders(HTTP_OK, 0);
                sendResponseBody(exchange, gson.toJson(errorMessage));
                return;
            }
        } else {
            try {
                personsSuccess = true;
                PersonsService personsService = new PersonsService();
                personsResult = personsService.findPersons(authToken);
            } catch (ServerError e) {
                errorMessage = new MessageResult("Error. There are no persons here.");
                exchange.sendResponseHeaders(HTTP_OK, 0);
                sendResponseBody(exchange, gson.toJson(errorMessage));
                return;
            }
        }

        if (personSuccess) {
            serverResponse = gson.toJson(personResult);
        }
        if (personsSuccess) {
            serverResponse = gson.toJson(personsResult);
        }

        exchange.sendResponseHeaders(HTTP_OK, 0);
        sendResponseBody(exchange, serverResponse);
    }
}