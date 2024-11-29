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


}
