package handler;

import error.ServerError;
import result.LoginResult;
import java.io.IOException;
import request.LoginRequest;
import result.MessageResult;
import service.LoginService;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import static java.net.HttpURLConnection.HTTP_OK;

public class LoginHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new Gson();
        boolean loginSuccess;
        String serverResponse;

        MessageResult errorMessage = null;
        LoginResult loginResult = null;

        try {
            loginSuccess = true;
            LoginRequest loginRequest = gson.fromJson(getRequestBody(exchange), LoginRequest.class);
            LoginService loginService = new LoginService();
            loginResult = loginService.loginUser(loginRequest);
        } catch (ServerError e) {
            loginSuccess = false;
            errorMessage = new MessageResult("Error. The username and password were not found.");
        }

        if (loginSuccess) {
            serverResponse = gson.toJson(loginResult);
        } else {
            serverResponse = gson.toJson(errorMessage);
        }

        exchange.sendResponseHeaders(HTTP_OK, 0);
        System.out.println(serverResponse);
        sendResponseBody(exchange, serverResponse);
    }
}