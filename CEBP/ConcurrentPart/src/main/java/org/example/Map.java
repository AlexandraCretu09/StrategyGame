package org.example;

public class Map {
    private int[][] terrain;
    private static int noOfPlayers;


}

// Since the map is the main thing shared between the threads, it will have to be the best synchronized

// ToDo: Map randomization of the resources and spawn point of the players
// ToDo: Good synchronization across all thread requests