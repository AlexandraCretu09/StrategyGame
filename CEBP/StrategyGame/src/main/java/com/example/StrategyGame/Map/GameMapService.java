package com.example.StrategyGame.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    public GameMap gameMapUpdater(int[][] terrain, GameMap gameMap){
        if (terrain == null || terrain.length == 0 || terrain[0].length == 0) {
            throw new IllegalArgumentException("Invalid terrain: it must be a non-empty 2D array.");
        }
        gameMap.setTerrain(terrain);
        return gameMap;

    }
}
