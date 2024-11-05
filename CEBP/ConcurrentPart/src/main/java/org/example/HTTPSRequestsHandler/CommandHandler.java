package org.example.HTTPSRequestsHandler;

import org.json.JSONObject;
import static spark.Spark.*;
public class CommandHandler {
    public static void registerRoutes() {
        post("/api/commands", (request, response) -> {
            // Parse JSON from the request body
            JSONObject json = new JSONObject(request.body());
            String username = json.optString("username");
            String command = json.optString("command");

            System.out.println("Received command from " + username + ": " + command);

            response.status(200);
            return "Command processed";
        });
    }
}