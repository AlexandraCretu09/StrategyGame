package com.example.StrategyGame.Map;

import jakarta.persistence.*;

@Entity
public class GameMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mapId;

    private int mapHeight;
    private int mapWidth;

    @Lob
    private int[][] terrain;


    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int[][] getTerrain() {
        return terrain;
    }

    public void setTerrain(int[][] terrain) {
        this.terrain = terrain;
    }
}
