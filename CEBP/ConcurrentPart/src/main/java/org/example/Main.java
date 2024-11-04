package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Define the dimensions of the map and the number of players
        int rows = 15;  // Map height
        int cols = 15;  // Map width
        int noOfPlayers = 2;

        // Create a new Map object
        Map gameMap = new Map(rows, cols, noOfPlayers);

        // Print the generated map
        System.out.println("Generated Map:");
        gameMap.printMap();


        // List to store player threads
        List<PlayerThreads> players = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (int playerId = 1; playerId <= noOfPlayers; playerId++) {


            int[] startingPosition = gameMap.getPlayerStartingPosition(playerId);
            int startX = startingPosition[0];
            int startY = startingPosition[1];

            // Create PlayerThreads with Map, playerId, startX, and startY
            int[] commandsPlayer;
            if(playerId == 1){
                commandsPlayer = new int[]{1,1,1,1,5};
            }else
                commandsPlayer = new int[]{2,2,2,2,5};

            PlayerThreads playerThread = new PlayerThreads(gameMap, playerId, startX, startY, commandsPlayer);
            Thread thread = new Thread(playerThread);
            players.add(playerThread);
            threads.add(thread);
            thread.start();
        }


        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
