package org.example;

import org.example.Exceptions.EmptyUserListException;
import org.example.HTTPSRequestsHandler.CommandHandler;
import org.example.HTTPSRequestsHandler.Initializer;
import org.example.HTTPSRequestsHandler.GameMapSenderRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
public class Main {

    private static final ConcurrentHashMap<String, BlockingQueue<String>> userQueues = new ConcurrentHashMap<>();
    private static GameMap gameMap;
    private static int lobbyId;
    private static List<Resource> resources = new ArrayList<>();

    public static void main(String[] args) {

        List<User> users = Initializer.runInit();
        if (users.isEmpty())
            throw new EmptyUserListException();

        int noOfPlayers = users.size();
        System.out.println(noOfPlayers);

        for (User u : users) {
            System.out.println(u.getPlayerId() + " " + u.getUsername());
        }

        gameMap = initMap(noOfPlayers, users);
        resources = gameMap.getResources();

        CommandHandler.endUserCommands();
        CommandHandler.receiveUsernameAndCommand();

        sendTerrainToSpringCaller();

        runThreads(users, gameMap);
    }

    public static void storeUserAndCommand(String username, String commandJson) {
        BlockingQueue<String> queue = userQueues.get(username);
        if (queue != null) {
            synchronized (queue) {
                queue.offer(commandJson); // Pass the full JSON string
            }
        } else {
            System.err.println("No queue found for username: " + username);
        }
    }

    public static void setLobbyId(int Id){
        lobbyId = Id;
    }

    public static void sendTerrainToSpringCaller() {
        try {
            synchronized (gameMap) {
                GameMapSenderRequest.sendTerrainToSpring(gameMap, lobbyId, resources);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static GameMap initMap(int noOfPlayers, List<User> users) {
        // Define the dimensions of the map and the number of players
        int rows = 15;  // Map height
        int cols = 15;  // Map width

        // Create a new Map object
        GameMap gameMap = new GameMap(rows, cols, noOfPlayers, users);

        // Print the generated map
        System.out.println("Generated Map:");
        gameMap.printMap();

        return gameMap;
    }

    private static void runThreads(List<User> users, GameMap gameMap) {

        // List to store player threads
        List<PlayerThreads> players = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (User u : users) {
            int[] startingPosition = gameMap.getPlayerStartingPosition(u.getPlayerId());
            int startX = startingPosition[0];
            int startY = startingPosition[1];

            String username = u.getUsername();
            userQueues.put(username, new LinkedBlockingQueue<>());
            System.out.println("Initialized queue for username: " + username);

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
