package handler;

import java.util.Scanner;
import java.io.PrintWriter;
import com.sun.net.httpserver.HttpExchange;

class Handler {

    String getRequestBody(HttpExchange exchange) {

        StringBuilder requestBody = new StringBuilder();
        Scanner inputScanner = new Scanner(exchange.getRequestBody());

        while (inputScanner.hasNextLine()) {
            requestBody.append(inputScanner.nextLine());
        }
        inputScanner.close();
        return requestBody.toString();
    }

    void sendResponseBody(HttpExchange exchange, String response) {

        PrintWriter responseBody = new PrintWriter(exchange.getResponseBody());
        responseBody.print(response);
        responseBody.close();
    }
}