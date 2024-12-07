package org.example;

import java.util.concurrent.BlockingQueue;
import org.json.JSONObject;

public class PlayerThreads implements Runnable {
    private GameMap gameMap;
    private int i, j;  // Current position of the player
    private int playerId;
    private final BlockingQueue<String> queue;
    private boolean running = true;

    public PlayerThreads(GameMap gameMap, int playerId, int startX, int startY, BlockingQueue<String> queue) {
        this.gameMap = gameMap;
        this.playerId = playerId;
        int[] startingPosition = gameMap.getPlayerStartingPosition(playerId);
        this.i = startingPosition[0];
        this.j = startingPosition[1];
        this.queue =  queue;
    }

    @Override
    public void run() {
        while (running) {
            String command = null;
            try {
                command = queue.take();
                if(command.isEmpty()){
                    System.err.println("Couldn't receive command");
                    continue;
                }
                if (command.equals("end"))
                    break;

                moveOnTheMap(command);
                Main.sendTerrainToSpringCaller();
                System.out.println("Received the command: " + command);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void moveLeft() {
        if (i > 0 && gameMap.movePlayer(playerId, i, j, i - 1, j)) {
            i -= 1;
        }else if (i <= 0)
            System.err.println("Position out of map bounds!");
    }

    public void moveRight() {
        if (i < gameMap.getHeight() - 1 && gameMap.movePlayer(playerId, i, j, i + 1, j)) {
            i += 1;
        } else if (i >= gameMap.getHeight())
            System.err.println("Position out of map bounds!");
    }

    public void moveUp() {
        if (j > 0 && gameMap.movePlayer(playerId, i, j, i, j - 1)) {
            j -= 1;
        }else if (j <= 0)
            System.err.println("Position out of map bounds!");
    }

    public void moveDown() {
        if (j < gameMap.getWidth() - 1 && gameMap.movePlayer(playerId, i, j, i, j + 1)) {
            j += 1;
        }else if (j >= gameMap.getHeight())
            System.err.println("Position out of map bounds!");
    }
    public void placeHouse(int x, int y, String resourceType) {
        System.out.printf("Player %d attempting to place house at (%d, %d) with resource type '%s'.%n",
                playerId, x, y, resourceType);

        boolean success = gameMap.placeHouse(
                playerId,
                x, // Use provided X coordinate
                y, // Use provided Y coordinate
                resourceType
        );

        if (success) {
            System.out.println("House placed successfully at (" + x + ", " + y + ").");
        } else {
            System.err.println("Failed to place house at (" + x + ", " + y + "). Ensure resource type and location match.");
        }
    }


    public void upgradeHouse() {
        boolean upgraded = gameMap.upgradeHouse(
                playerId,
                i, // Current X position
                j // Current Y position
        );
        System.out.println(upgraded ? "House upgraded successfully." : "Failed to upgrade house.");
    }

    public void destroyHouse() {
        boolean destroyed = gameMap.destroyHouse(
                playerId,
                i, // Current X position
                j // Current Y position
        );
        System.out.println(destroyed ? "House destroyed successfully." : "Failed to destroy house.");
    }
    public void moveOnTheMap(String command) {
        try {
            // Log the received command for debugging
            System.out.println("Received command string: " + command);

            // Parse the JSON command
            JSONObject commandJson = new JSONObject(command);
            String cmd = commandJson.getString("command");

            switch (cmd) {
                case "moveUp" -> moveUp();
                case "moveDown" -> moveDown();
                case "moveLeft" -> moveLeft();
                case "moveRight" -> moveRight();
                case "placeHouse" -> {
                    // Extract position and resource type
                    JSONObject position = commandJson.getJSONObject("position");
                    int x = position.getInt("x");
                    int y = position.getInt("y");
                    String resourceType = commandJson.getString("resourceType");

                    // Call the placeHouse method
                    placeHouse(x, y, resourceType);
                }
                case "end" -> stopMoving();
                default -> System.err.println("Invalid command: " + cmd);
            }
        } catch (org.json.JSONException e) {
            System.err.println("Invalid JSON command: " + command);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameMap.printMap();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void stopMoving() {
        running = false;
        try{
            queue.put("end");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
