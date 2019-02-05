package handler;

import error.ServerError;
import result.LoginResult;
import java.io.IOException;
import result.MessageResult;
import request.RegisterRequest;
import service.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import static java.net.HttpURLConnection.HTTP_OK;

public class RegisterHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new Gson();
        boolean registerSuccess;
        String serverResponse;

        MessageResult errorMessage = null;
        LoginResult loginResult = null;

        try {
            registerSuccess = true;
            RegisterRequest registerRequest = gson.fromJson(getRequestBody(exchange), RegisterRequest.class);
            RegisterService registerService = new RegisterService();
            loginResult = registerService.registerUser(registerRequest);
        } catch (ServerError e) {
            registerSuccess = false;
            errorMessage = new MessageResult(e.getMessage());
        }

        if (registerSuccess) {
            serverResponse = gson.toJson(loginResult);
        } else {
            serverResponse = gson.toJson(errorMessage);
        }

        exchange.sendResponseHeaders(HTTP_OK, 0);
        System.out.println(serverResponse);
        sendResponseBody(exchange, serverResponse);
    }
}