package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Define the dimensions of the map and the number of players
        int rows = 15;  // Map height
        int cols = 15;  // Map width
        int noOfPlayers = 3;  // Number of players

//        // Create a new Map object
//        Map gameMap = new Map(rows, cols, noOfPlayers);
//
//        // Print the generated map
//        System.out.println("Generated Map:");
//        gameMap.printMap();
//
//        // Display resources
//        System.out.println("\nResources:");
//        for (Resource resource : gameMap.getResources()) {
//            System.out.println(resource);
//        }

        int[][] matrix = new int[5][5];


        PlayerThreads player1 = new PlayerThreads(matrix, "1", 3, 4);
        //PlayerThreads player2 = new PlayerThreads(matrix, "2", 0, 2);

        Thread thread1 = new Thread(player1);
        //

        // Start the thread
        thread1.start();

        Scanner obj = new Scanner(System.in);
        int input;

        int[] inputCommandsPlayer1, inputCommandsPlayer2;

        inputCommandsPlayer1 = new int[]{1, 4, 3, 5};
        inputCommandsPlayer2 = new int[]{ 2, 2, 3, 1};

        while (true) {
            input = obj.nextInt();
            if (input == 5) {
                player1.stopMoving();
                break;
            } else {

                switch (input) {
                    case 1:
                        player1.moveUp();
                        player1.printMap();
                        break;
                    case 2:
                        player1.moveDown();
                        player1.printMap();
                        break;
                    case 3:
                        player1.moveLeft();
                        player1.printMap();
                        break;
                    case 4:
                        player1.moveRight();
                        player1.printMap();
                        break;
                    default: System.out.println("Invalid input!");
                }
            }
        }

        // Wait for the thread to finish
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
