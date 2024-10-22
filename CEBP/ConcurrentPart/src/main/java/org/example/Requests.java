package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Requests {

    public static void main(String[] args) throws IOException {
        // Create the server, listening on port 8081
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/handle-command", new CommandHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Logic Project running on http://localhost:8081");
    }

    // Handler to process requests
    static class CommandHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read the request body (command input)
                byte[] requestBytes = exchange.getRequestBody().readAllBytes();
                String requestBody = new String(requestBytes);

                // Logic processing
                String response = "Processed Command: " + requestBody;

                // Send response
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
            }
        }
    }
}
