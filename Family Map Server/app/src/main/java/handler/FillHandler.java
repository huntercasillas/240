package handler;

import java.net.URI;
import java.io.IOException;

import error.ServerError;
import service.FillService;
import result.MessageResult;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import static java.net.HttpURLConnection.HTTP_OK;

public class FillHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new Gson();
        int generations = 0;
        MessageResult serverResponse;

        URI requestURI = exchange.getRequestURI();
        String[] splitURI = requestURI.getPath().split("/");

        if (splitURI.length < 3) {
            serverResponse = new MessageResult("Error. You must enter a valid username.");
            exchange.sendResponseHeaders(HTTP_OK, 0);
            sendResponseBody(exchange, gson.toJson(serverResponse));
            return;
        }

        if (splitURI.length == 4) {
            try {
                generations = Integer.parseInt(splitURI[3]);
            } catch (Exception e) {
                serverResponse = new MessageResult("Error. You must enter a valid generation number.");
                exchange.sendResponseHeaders(HTTP_OK, 0);
                sendResponseBody(exchange, gson.toJson(serverResponse));
                return;
            }
        }

        try {
            FillService fillService = new FillService();
            serverResponse = fillService.fillInformation(splitURI[2], generations);
        } catch (ServerError e) {
            serverResponse = new MessageResult(e.getMessage());
        }

        exchange.sendResponseHeaders(HTTP_OK, 0);
        sendResponseBody(exchange, gson.toJson(serverResponse));
    }
}