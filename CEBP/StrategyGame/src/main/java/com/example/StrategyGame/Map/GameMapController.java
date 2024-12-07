package com.example.StrategyGame.Map;

import com.example.StrategyGame.Unity.UnityWebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/gameMap")
public class GameMapController {

    @Autowired
    private GameMapService gameMapService;

    private GameMap gameMap;
    private static boolean mapCreated = false;


    @PostMapping("/update-terrain")
    public ResponseEntity<String> updateTerrain(@RequestBody int[][] terrain) {

        if(!mapCreated){
            gameMap = gameMapService.gameMapCreator(terrain);
            mapCreated = true;

        }else {
            gameMap = gameMapService.gameMapUpdater(terrain, gameMap);
        }
        UnityWebSocketClient client = new UnityWebSocketClient(URI.create("ws://localhost:8082/terrainUpdate"));

        try {
            client.connect();
            int retries = 0;
            while (!client.isOpen() && retries < 5) {
                Thread.sleep(1000);
                retries++;
            }

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

        return ResponseEntity.ok("Terrain updated successfully!");
    }
}
