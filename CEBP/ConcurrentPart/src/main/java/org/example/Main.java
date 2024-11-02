package main.java.org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Define the dimensions of the map and the number of players
        int rows = 15;  // Map height
        int cols = 15;  // Map width
        int noOfPlayers = 3;  // Number of players

        // Create a new Map object
        Map gameMap = new Map(rows, cols, noOfPlayers);

        // Print the generated map
        System.out.println("Generated Map:");
        gameMap.printMap();

        // Display resources
        System.out.println("\nResources:");
        for (Resource resource : gameMap.getResources()) {
            System.out.println(resource);
        }

        // List to store player threads
        List<PlayerThreads> players = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (int playerId = 1; playerId <= noOfPlayers; playerId++) {
            // Get the starting position for the player
            int[] startingPosition = gameMap.getPlayerStartingPosition(playerId);
            int startX = startingPosition[0];
            int startY = startingPosition[1];

            // Create PlayerThreads with Map, playerId, startX, and startY
            PlayerThreads playerThread = new PlayerThreads(gameMap, playerId, startX, startY);
            Thread thread = new Thread(playerThread);
            players.add(playerThread);
            threads.add(thread);
            thread.start(); // Start each player thread
        }


        // Scanner for reading input
        Scanner scanner = new Scanner(System.in);

        // Control loop
        while (true) {
            System.out.println("Enter player number and direction (1=up, 2=down, 3=left, 4=right, 5=exit): ");
            int playerId = scanner.nextInt();
            int direction = scanner.nextInt();

            // Exit condition
            if (direction == 5) {
                for (PlayerThreads player : players) {
                    player.stopMoving();
                }
                break;
            }

            // Validate player ID and move
            if (playerId > 0 && playerId <= noOfPlayers) {
                PlayerThreads player = players.get(playerId - 1);
                switch (direction) {
                    case 1 -> player.moveUp();
                    case 2 -> player.moveDown();
                    case 3 -> player.moveLeft();
                    case 4 -> player.moveRight();
                    default -> System.out.println("Invalid direction!");
                }
            } else {
                System.out.println("Invalid player ID!");
            }

            // Print map after each move
            System.out.println("Updated Map:");
            gameMap.printMap();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        scanner.close();
    }
}
