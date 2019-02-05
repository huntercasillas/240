package handler;

import java.net.URI;
import error.ServerError;
import result.EventResult;
import result.EventsResult;
import java.io.IOException;
import result.MessageResult;
import service.EventService;
import com.google.gson.Gson;
import service.EventsService;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import static java.net.HttpURLConnection.HTTP_OK;

public class EventHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new Gson();
        MessageResult errorMessage = new MessageResult("Internal Server Error.");
        String serverResponse = gson.toJson(errorMessage);

        EventResult eventResult = null;
        boolean eventSuccess = false;

        EventsResult eventsResult = null;
        boolean eventsSuccess = false;

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
                eventSuccess = true;
                EventService eventService = new EventService();
                eventResult = eventService.findEvent(authToken, splitURI[2]);
            } catch (ServerError e) {
                errorMessage = new MessageResult("Error. The requested event does not exist.");
                exchange.sendResponseHeaders(HTTP_OK, 0);
                sendResponseBody(exchange, gson.toJson(errorMessage));
                return;
            }
        } else {
            try {
                eventsSuccess = true;
                EventsService eventsRequest = new EventsService();
                eventsResult = eventsRequest.findEvents(authToken);
            } catch (ServerError e) {
                errorMessage = new MessageResult("Error. There are no events here.");
                exchange.sendResponseHeaders(HTTP_OK, 0);
                sendResponseBody(exchange, gson.toJson(errorMessage));
                return;
            }
        }

        if (eventSuccess) {
            serverResponse = gson.toJson(eventResult);
        }
        if (eventsSuccess) {
            serverResponse = gson.toJson(eventsResult);
        }

        exchange.sendResponseHeaders(HTTP_OK, 0);
        sendResponseBody(exchange, serverResponse);
    }
}