package com.example.StrategyGame.Map;

import com.example.StrategyGame.CustomExceptions.FailedToConnectToClient;
import com.example.StrategyGame.SimpleRequestClasses.Resources;
import com.example.StrategyGame.Unity.UnityWebSocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameMapService {

    public GameMap gameMapCreator(int[][] terrain){

        if (terrain == null || terrain.length == 0 || terrain[0].length == 0) {
            throw new IllegalArgumentException("Invalid terrain: it must be a non-empty 2D array.");
        }
        GameMap gameMap = new GameMap();

        gameMap.setTerrain(terrain);
        gameMap.setMapHeight(terrain.length);
        gameMap.setMapWidth(terrain[0].length);
        return gameMap;

    }

    public void sendToEachFrontend(List<String> lobbyIDs, GameMap gameMap, List<Resources> resources) {
        for (String IP : lobbyIDs) {
            UnityWebSocketClient client = new UnityWebSocketClient(URI.create("ws://" + IP + ":8082/terrainUpdate"));
            //System.out.println("Client id:     " + "ws://" + IP + ":8082/terrainUpdate");

            try {
                client.connect();
                int retries = 0;
                do {
                    retries++;
                    Thread.sleep(2000);
                } while (!client.isOpen() && retries < 15);

                if (client.isOpen()) {
                    client.sendGameMap(gameMap, resources);
                } else {
                    throw new FailedToConnectToClient("WebSocket connection not open. " + lobbyIDs);
                }
            } catch (Exception e) {
                throw new FailedToConnectToClient("Failed to update terrain. " + lobbyIDs);
            } finally {
                client.close();
            }
        }
    }

    public void checkResources(List<Resources> resources){
        if(resources.isEmpty())
            throw new IllegalArgumentException("Invalid list of resources: it must be a non-empty List");
    }

    public void printResources(List<Resources> resources){
        for(Resources r : resources){
            System.out.println("Coords: x: "+ r.getX() + " y: "+ r.getY() + " type: " + r.getType());
        }
    }



    public GameMap gameMapUpdater(int[][] terrain, GameMap gameMap){
        if (terrain == null || terrain.length == 0 || terrain[0].length == 0) {
            throw new IllegalArgumentException("Invalid terrain: it must be a non-empty 2D array.");
        }
        gameMap.setTerrain(terrain);
        return gameMap;

    }
}
