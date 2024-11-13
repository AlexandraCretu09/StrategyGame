package org.example;

import org.example.HTTPSRequestsHandler.CommandHandler;
import org.example.HTTPSRequestsHandler.Initializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static spark.Spark.port;


public class Main {

    private static final ConcurrentHashMap<String, List<String>> userCommands = new ConcurrentHashMap<>();
    public static void main(String[] args) {


//        List<User> users = Initializer.runInit();
//        int noOfPlayers = users.size();
//
//        System.out.println(noOfPlayers);
//
//        for (User u : users) {
//            System.out.println(u.getPlayerId() + " " + u.getUsername());
//        }

        port(8081);

        CommandHandler.endUserCommands();
        CommandHandler.registerRoutes();

    }

    public static synchronized void handleCommand(String username, String command) {
        userCommands.computeIfAbsent(username, k -> new ArrayList<>()).add(command);

    }

    public static synchronized void printList() {
        if (userCommands.isEmpty()) {
            System.out.println("List is empty");
        } else {
            userCommands.forEach((username, commands) -> {
                commands.forEach(command -> {
                    System.out.println("Username: " + username + ", Command: " + command);
                });
            });
        }
    }




/*
    public static void runThreads(){

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
*/
}
