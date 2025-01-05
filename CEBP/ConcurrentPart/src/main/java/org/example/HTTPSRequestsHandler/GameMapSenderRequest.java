package org.example.HTTPSRequestsHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.GameMap;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class GameMapSenderRequest {

    public static void sendTerrainToSpring(GameMap gameMap, int lobbyId) {
        try {

            String terrainJson = new ObjectMapper().writeValueAsString(Map.of(
                    "terrain", gameMap.getTerrain(),
                    "lobbyId", lobbyId
            ));

//            JSONObject payload = new JSONObject();
//            payload.put("terrain", gameMap.terrainToJSON());
//            payload.put("lobbyId", lobbyId);
//
//            String payloadJson = payload.toString();

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
