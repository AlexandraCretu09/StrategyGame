package org.example.HTTPSRequestsHandler;

import org.example.GameMap;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.*;

public class GameMapSenderRequest {

    public static void sendTerrainToSpring(GameMap gameMap) {
        try {
            String terrainJson = gameMap.terrainToJSON();
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/gameMap/update-terrain"))
                    .POST(HttpRequest.BodyPublishers.ofString(terrainJson))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Terrain sent successfully!");
            } else {
                System.out.println("Failed to send terrain: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
