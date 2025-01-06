package com.example.StrategyGame.SimpleRequestClasses;

import java.util.List;

public class IdResourcesAndMap {
    private List<List<Integer>>terrain;
    private List<Resources> resources;
    private int lobbyId;

    public IdResourcesAndMap(List<List<Integer>> terrain, int lobbyId, List<Resources> resources) {
        this.terrain = terrain;
        this.lobbyId = lobbyId;
        this.resources = resources;
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

    public List<Resources> getResources() {
        return resources;
    }

    public void setResources(List<Resources> resources) {
        this.resources = resources;
    }
}
