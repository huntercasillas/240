package handler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.net.HttpURLConnection;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class DefaultHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String error404 = new String(Files.readAllBytes(Paths.get("data/web/HTML/404.html")));

        try {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            String requestURI = exchange.getRequestURI().toString();
            String defaultURI = "data/web" + requestURI;

            if (requestURI.equals("/")) {
                defaultURI += "index.html";
            }
            Path filePath = FileSystems.getDefault().getPath(defaultURI);

            Files.copy(filePath, exchange.getResponseBody());
            exchange.getResponseBody().close();

        } catch (IOException e) {
            sendResponseBody(exchange, error404);
        }
    }
}