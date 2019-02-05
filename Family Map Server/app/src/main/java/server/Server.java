package server;

import handler.*;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

/**
 * Class that creates and starts the server.
 */
public class Server {

    public static void main(String[] args) {

        Server familyServer = new Server();
        int portNumber = 8080;
        familyServer.startServer(portNumber);

    }

    private void startServer(int portNumber) {

        System.out.println("Starting server on port: " + portNumber + "\n");

        HttpServer familyServer;

        try {
            familyServer = HttpServer.create(new InetSocketAddress(portNumber), 0);
        }
        catch (IOException e) {
            System.out.println("Error. The Server failed to start.");
            return;
        }

        familyServer.createContext("/", new DefaultHandler());
        familyServer.createContext("/user/register", new RegisterHandler());
        familyServer.createContext("/user/login", new LoginHandler());
        familyServer.createContext("/clear", new ClearHandler());
        familyServer.createContext("/fill", new FillHandler());
        familyServer.createContext("/load", new LoadHandler());
        familyServer.createContext("/person", new PersonHandler());
        familyServer.createContext("/event", new EventHandler());
        familyServer.start();
    }
}