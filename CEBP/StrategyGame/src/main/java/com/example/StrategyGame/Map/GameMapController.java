package com.example.StrategyGame.Map;

import com.example.StrategyGame.Lobby.Lobby;
import com.example.StrategyGame.Lobby.LobbyService;
import com.example.StrategyGame.SimpleRequestClasses.MapAndID;
import com.example.StrategyGame.Unity.UnityWebSocketClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/gameMap")
public class GameMapController {

    @Autowired
    private GameMapService gameMapService;

    @Autowired
    private LobbyService lobbyService;

    private GameMap gameMap;
    private static boolean mapCreated = false;


    @PostMapping("/update-terrain")
    public ResponseEntity<String> updateTerrain(@RequestBody MapAndID request) throws JsonProcessingException {

        //System.out.println("Received JSON: " + new ObjectMapper().writeValueAsString(request));

        List<List<Integer>> terrainList = request.getTerrain();
        int[][] terrain = terrainList.stream()
                .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
                .toArray(int[][]::new);

        int lobbyId = request.getLobbyId();
        List<String> lobbyIDs = lobbyService.getAllLobbyIPs(lobbyId);

        if (!mapCreated) {
            gameMap = gameMapService.gameMapCreator(terrain);
            mapCreated = true;

        } else {
            gameMap = gameMapService.gameMapUpdater(terrain, gameMap);
        }



        for (String IP : lobbyIDs) {
            UnityWebSocketClient client = new UnityWebSocketClient(URI.create("ws://" + IP +":8082/terrainUpdate"));
            System.out.println("Client id:     "+ "ws://" + IP +":8082/terrainUpdate");

            try {
                client.connect();
                int retries = 0;
                do  {
                    retries++;
                    Thread.sleep(2000);
                } while(!client.isOpen() && retries < 15);

                if (client.isOpen()) {
                    client.sendGameMap(gameMap);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("WebSocket connection not open.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update terrain");
            } finally {
                client.close();
            }
        }
        return ResponseEntity.ok("Terrain updated successfully!");
    }
}
