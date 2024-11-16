package org.example;

import org.example.Exceptions.EmptyUserListException;
import org.example.HTTPSRequestsHandler.CommandHandler;
import org.example.HTTPSRequestsHandler.Initializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static spark.Spark.port;


public class Main {

    private static final ConcurrentHashMap<String, List<String>> userCommands = new ConcurrentHashMap<>();

    private static BlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();
    public static void main(String[] args) {


        List<User> users = Initializer.runInit();

        if(users.isEmpty())
            throw new EmptyUserListException();

        int noOfPlayers = users.size();

        System.out.println(noOfPlayers);

        for (User u : users) {
            System.out.println(u.getPlayerId() + " " + u.getUsername());
        }

        Map gameMap = initMap(noOfPlayers);

        CommandHandler.endUserCommands();
        CommandHandler.registerRoutes();
        CommandHandler.sendOneCommand();


        runThreads(noOfPlayers, gameMap);



    }

    public static synchronized void storeUserAndCommand(String username, String command) {
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

    public static Map initMap(int noOfPlayers){
        // Define the dimensions of the map and the number of players
        int rows = 15;  // Map height
        int cols = 15;  // Map width

        // Create a new Map object
        Map gameMap = new Map(rows, cols, noOfPlayers);

        // Print the generated map
        System.out.println("Generated Map:");
        gameMap.printMap();

        return gameMap;
    }

    public static void storeCommand(String command){
        try {
            commandQueue.put(command);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }





    public static void runThreads(int noOfPlayers, Map gameMap){




        // List to store player threads
        List<PlayerThreads> players = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (int playerId = 1; playerId <= noOfPlayers; playerId++) {
            int[] startingPosition = gameMap.getPlayerStartingPosition(playerId);
            int startX = startingPosition[0];
            int startY = startingPosition[1];

            PlayerThreads playerThread = new PlayerThreads(gameMap, playerId, startX, startY, commandQueue);
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
