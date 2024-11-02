package main.java.org.example;

import java.util.Random;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import java.util.List;


public class Map {
    private int[][] terrain; // 0 for empty, -1 for resource, >0 for player ID
    private final Lock[][] cellLocks; // Locks for each cell to prevent concurrent access
    private static int noOfPlayers;
    private List<Resource> resources;
    private int mapWidth;
    private int mapHeight;

    // Resource types
    private static final String[] resourceTypes = {"wood", "stone", "gold"};
    private List<int[]> playerStartingPositions;

    public Map(int rows, int cols, int noOfPlayers) {
        this.terrain = new int[rows][cols];
        this.cellLocks = new ReentrantLock[rows][cols]; // Create a lock for each cell
        this.mapHeight = rows;
        this.mapWidth = cols;
        Map.noOfPlayers = noOfPlayers;
        this.resources = new CopyOnWriteArrayList<>();
        playerStartingPositions = new ArrayList<>(noOfPlayers);

        // Initialize locks for each cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellLocks[i][j] = new ReentrantLock();
            }
        }

        initializeMap();

    }

    // Add these methods in the Map class

    public int getHeight() {
        return mapHeight;
    }

    public int getWidth() {
        return mapWidth;
    }

    private void initializeMap() {
        Random random = new Random();
        int bufferZone = 3;
        int outerMargin = 1;
        int innerRestrictedAreaMargin = Math.min(mapWidth, mapHeight) / 2;

        int spawnAreaStartX = outerMargin;
        int spawnAreaEndX = mapWidth - outerMargin - 1;
        int spawnAreaStartY = outerMargin;
        int spawnAreaEndY = mapHeight - outerMargin - 1;

        int restrictedAreaStartX = (mapWidth / 2) - innerRestrictedAreaMargin / 2;
        int restrictedAreaEndX = (mapWidth / 2) + innerRestrictedAreaMargin / 2;
        int restrictedAreaStartY = (mapHeight / 2) - innerRestrictedAreaMargin / 2;
        int restrictedAreaEndY = (mapHeight / 2) + innerRestrictedAreaMargin / 2;

        int maxAttempts = mapWidth * mapHeight * 4 / noOfPlayers;

        for (int i = 1; i <= noOfPlayers; i++) {
            int x = 0, y = 0;
            boolean validSpawn = false;
            int attempts = 0;

            while (!validSpawn && attempts < maxAttempts) {
                attempts++;
                x = random.nextInt(spawnAreaEndX - spawnAreaStartX + 1) + spawnAreaStartX;
                y = random.nextInt(spawnAreaEndY - spawnAreaStartY + 1) + spawnAreaStartY;

                if (x >= restrictedAreaStartX && x <= restrictedAreaEndX &&
                        y >= restrictedAreaStartY && y <= restrictedAreaEndY) {
                    continue;
                }

                validSpawn = true;

                for (int dx = -bufferZone; dx <= bufferZone; dx++) {
                    for (int dy = -bufferZone; dy <= bufferZone; dy++) {
                        int checkX = x + dx;
                        int checkY = y + dy;

                        if (checkX >= 0 && checkX < mapWidth && checkY >= 0 && checkY < mapHeight) {
                            if (terrain[checkY][checkX] != 0) {
                                validSpawn = false;
                                break;
                            }
                        }
                    }
                    if (!validSpawn) break;
                }
            }

            if (!validSpawn) {
                System.err.println("Unable to find a valid spawn location for player " + i + " after " + attempts + " attempts.");
                continue;
            }

            terrain[y][x] = i;
            playerStartingPositions.add(new int[] { x, y }); // Store starting position
            System.out.println("Player " + i + " spawn point: (" + x + ", " + y + ")");
            placeResourcesNear(x, y, 3);
        }
        placeRandomResources(18);
    }

    public int[] getPlayerStartingPosition(int playerId) {
        return playerStartingPositions.get(playerId - 1); // Adjusting for zero index
    }



    private void placeResourcesNear(int x, int y, int numberOfResources) {
        Random random = new Random();

        for (int i = 0; i < numberOfResources; i++) {
            int dx, dy;

            do {
                dx = Math.max(0, Math.min(mapWidth - 1, x + random.nextInt(5) - 2));
                dy = Math.max(0, Math.min(mapHeight - 1, y + random.nextInt(5) - 2));
            } while (terrain[dy][dx] != 0);

            String resourceType = resourceTypes[random.nextInt(resourceTypes.length)];
            Resource resource = new Resource(resourceType, dx, dy);
            resources.add(resource);
            terrain[dy][dx] = -1;
            System.out.println("Placed resource: " + resource);
        }
    }

    private void placeRandomResources(int numberOfResources) {
        Random random = new Random();

        for (int i = 0; i < numberOfResources; i++) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);

            if (terrain[y][x] == 0) {
                String resourceType = resourceTypes[random.nextInt(resourceTypes.length)];
                Resource resource = new Resource(resourceType, x, y);
                resources.add(resource);
                terrain[y][x] = -1;
                System.out.println("Placed random resource: " + resource);
            }
        }
    }

    public void printMap() {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (terrain[i][j] == 0) {
                    System.out.print(". ");
                } else if (terrain[i][j] > 0) {
                    System.out.print("P" + terrain[i][j] + " ");
                } else {
                    System.out.print("R ");
                }
            }
            System.out.println();
        }
    }

    public List<Resource> getResources() {
        return resources;
    }

    /**
     * Synchronized method to move a player on the map.
     *
     * @param playerId The ID of the player moving.
     * @param oldX     The player's current X position.
     * @param oldY     The player's current Y position.
     * @param newX     The desired new X position.
     * @param newY     The desired new Y position.
     * @return true if the move was successful, false otherwise.
     */
    public boolean movePlayer(int playerId, int oldX, int oldY, int newX, int newY) {
        if (newX < 0 || newX >= mapWidth || newY < 0 || newY >= mapHeight) {
            System.out.println("Move out of bounds!");
            return false;
        }

        Lock newCellLock = cellLocks[newY][newX];
        newCellLock.lock();
        try {
            if (terrain[newY][newX] == 0) { // Check if the new position is empty
                // Attempt to move the player
                terrain[newY][newX] = playerId; // Set new position
                System.out.println("Moving player " + playerId + " from (" + oldX + ", " + oldY + ") to (" + newX + ", " + newY + ")");

                if (oldX != -1 && oldY != -1) { // If there is a valid old position
                    Lock oldCellLock = cellLocks[oldY][oldX];
                    oldCellLock.lock();
                    try {
                        // Here, we keep the old position occupied until we confirm the move is complete.
                        // This can be used to reflect conquered positions or to implement additional game logic.
                    } finally {
                        oldCellLock.unlock();
                    }
                }
                return true; // Move successful
            } else {
                System.out.println("Position occupied!");
                return false; // New position is occupied
            }
        } finally {
            newCellLock.unlock();
        }
    }
}


