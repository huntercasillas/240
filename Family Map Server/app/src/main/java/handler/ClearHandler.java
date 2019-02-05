package handler;

import error.ServerError;
import java.io.IOException;
import result.MessageResult;
import service.ClearService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import static java.net.HttpURLConnection.HTTP_OK;

public class ClearHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new Gson();
        MessageResult serverResponse;

        try {
            ClearService clearService = new ClearService();
            serverResponse = clearService.clearData();
        } catch (ServerError e) {
            serverResponse = new MessageResult(e.getMessage());
        }

        exchange.sendResponseHeaders(HTTP_OK, 0);
        sendResponseBody(exchange, gson.toJson(serverResponse));
    }
}