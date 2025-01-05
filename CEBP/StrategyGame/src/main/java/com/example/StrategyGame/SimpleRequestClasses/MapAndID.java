package com.example.StrategyGame.SimpleRequestClasses;

import java.util.List;

public class MapAndID {
    private List<List<Integer>>terrain;
    private int lobbyId;

    public MapAndID(List<List<Integer>> terrain, int lobbyId) {
        this.terrain = terrain;
        this.lobbyId = lobbyId;
    }

    public List<List<Integer>> getTerrain() {
        return terrain;
    }

    public void setTerrain(List<List<Integer>> terrain) {
        this.terrain = terrain;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }
}
