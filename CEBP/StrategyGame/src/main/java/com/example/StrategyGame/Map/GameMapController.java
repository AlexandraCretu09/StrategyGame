package com.example.StrategyGame.Map;

import com.example.StrategyGame.CustomExceptions.FailedToConnectToClient;
import com.example.StrategyGame.Lobby.LobbyService;
import com.example.StrategyGame.SimpleRequestClasses.IdResourcesAndMap;
import com.example.StrategyGame.SimpleRequestClasses.Resources;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> updateTerrain(@RequestBody IdResourcesAndMap request) throws JsonProcessingException {

        //System.out.println("Received JSON: " + new ObjectMapper().writeValueAsString(request));

        try {

            List<List<Integer>> terrainList = request.getTerrain();
            int[][] terrain = terrainList.stream()
                    .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
                    .toArray(int[][]::new);

            int lobbyId = request.getLobbyId();
            List<String> lobbyIDs = lobbyService.getAllLobbyIPs(lobbyId);

            List<Resources> resources = request.getResources();

            gameMapService.checkResources(resources);
            //gameMapService.printResources(resources);

            if (!mapCreated) {
                gameMap = gameMapService.gameMapCreator(terrain);
                mapCreated = true;

            } else {
                gameMap = gameMapService.gameMapUpdater(terrain, gameMap);
            }
            gameMapService.sendToEachFrontend(lobbyIDs, gameMap, resources);
            return ResponseEntity.ok("Terrain updated successfully!");

        } catch( FailedToConnectToClient e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch( IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
