package handler;

import java.io.IOException;

import error.ServerError;
import request.LoadRequest;
import service.LoadService;
import com.google.gson.Gson;
import result.MessageResult;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import static java.net.HttpURLConnection.HTTP_OK;

public class LoadHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new Gson();
        MessageResult serverResponse;

        try {
            LoadRequest loadRequest = gson.fromJson(getRequestBody(exchange), LoadRequest.class);
            LoadService loadService = new LoadService();
            serverResponse = loadService.loadInformation(loadRequest);
        } catch (JsonParseException e) {
            serverResponse = new MessageResult("There was an error parsing the JSON.");
        } catch (ServerError e) {
            serverResponse = new MessageResult(e.getMessage());
        }
        exchange.sendResponseHeaders(HTTP_OK, 0);
        sendResponseBody(exchange, gson.toJson(serverResponse));
    }
}