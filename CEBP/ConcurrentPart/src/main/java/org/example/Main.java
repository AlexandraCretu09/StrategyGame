package org.example;

import org.example.Exceptions.EmptyUserListException;
import org.example.HTTPSRequestsHandler.CommandHandler;
import org.example.HTTPSRequestsHandler.Initializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static spark.Spark.port;


public class Main {

    private static final ConcurrentHashMap<String, List<String>> userCommands = new ConcurrentHashMap<>();
    private static final Map<String, BlockingQueue<String>> userQueues = new ConcurrentHashMap<>();
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

        GameMap gameMap = initMap(noOfPlayers);

        CommandHandler.endUserCommands();
        CommandHandler.receiveUsernameAndCommand();
        CommandHandler.receiveOneCommand();


        runThreads(users, gameMap);



    }

    public static synchronized void storeUserAndCommand(String username, String command) {
        BlockingQueue<String> queue = userQueues.get(username);
        if (queue != null) {
            queue.offer(command); // Adds the command to the user's queue
        } else {
            System.err.println("No queue found for username: " + username);
        }
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

    public static GameMap initMap(int noOfPlayers){
        // Define the dimensions of the map and the number of players
        int rows = 15;  // Map height
        int cols = 15;  // Map width

        // Create a new Map object
        GameMap gameMap = new GameMap(rows, cols, noOfPlayers);

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





    private static void runThreads(List<User> users, GameMap gameMap){




        // List to store player threads
        List<PlayerThreads> players = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for(User u : users){
            int[] startingPosition = gameMap.getPlayerStartingPosition(u.getPlayerId());
            int startX = startingPosition[0];
            int startY = startingPosition[1];

            String username = u.getUsername();
            userQueues.put(username, new LinkedBlockingQueue<>());

            PlayerThreads playerThread = new PlayerThreads(gameMap, u.getPlayerId(), startX, startY, userQueues.get(username));

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
