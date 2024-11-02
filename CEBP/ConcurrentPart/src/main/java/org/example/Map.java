

package main.java.org.example;

import java.util.Random;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CopyOnWriteArrayList;

public class Map {
    private int[][] terrain;
    private static int noOfPlayers;
    private List<Resource> resources;
    private int mapWidth;
    private int mapHeight;

    private final Lock mapLock = new ReentrantLock();

    // Resource types
    private static final String[] resourceTypes = {"wood", "stone", "gold"};

    public Map(int rows, int cols, int noOfPlayers) {
        this.terrain = new int[rows][cols];
        this.mapHeight = rows;
        this.mapWidth = cols;
        Map.noOfPlayers = noOfPlayers;
        this.resources = new CopyOnWriteArrayList<>();
        initializeMap();
    }

    private void initializeMap() {
        Random random = new Random();
        int bufferZone = 3; // Buffer zone around players, creating a 3x3 grid (1 cell in all directions)
        int outerMargin = 1; // Outer margin for resources only
        int innerRestrictedAreaMargin = Math.min(mapWidth, mapHeight) / 2; // Define a central area dynamically

        // Define the bounds for the outer ring spawn area
        int spawnAreaStartX = outerMargin;
        int spawnAreaEndX = mapWidth - outerMargin - 1;
        int spawnAreaStartY = outerMargin;
        int spawnAreaEndY = mapHeight - outerMargin - 1;

        // Define the inner restricted area (players cannot spawn here)
        int restrictedAreaStartX = (mapWidth / 2) - innerRestrictedAreaMargin / 2;
        int restrictedAreaEndX = (mapWidth / 2) + innerRestrictedAreaMargin / 2;
        int restrictedAreaStartY = (mapHeight / 2) - innerRestrictedAreaMargin / 2;
        int restrictedAreaEndY = (mapHeight / 2) + innerRestrictedAreaMargin / 2;

        int maxAttempts = mapWidth * mapHeight * 4 / noOfPlayers;

        for (int i = 1; i <= noOfPlayers; i++) {
            int x = 0, y = 0;
            boolean validSpawn = false;
            int attempts = 0;

            // Try to find a valid spawn location within the outer ring spawn area
            while (!validSpawn && attempts < maxAttempts) {
                attempts++;
                x = random.nextInt(spawnAreaEndX - spawnAreaStartX + 1) + spawnAreaStartX;
                y = random.nextInt(spawnAreaEndY - spawnAreaStartY + 1) + spawnAreaStartY;

                // Ensure the location is not within the inner restricted area
                if (x >= restrictedAreaStartX && x <= restrictedAreaEndX &&
                        y >= restrictedAreaStartY && y <= restrictedAreaEndY) {
                    continue; // Skip this location if it's within the restricted area
                }

                validSpawn = true;

                // Check if the 3x3 area around the spawn point is clear
                for (int dx = -bufferZone; dx <= bufferZone; dx++) {
                    for (int dy = -bufferZone; dy <= bufferZone; dy++) {
                        int checkX = x + dx;
                        int checkY = y + dy;

                        // Ensure we are within bounds of the map
                        if (checkX >= 0 && checkX < mapWidth && checkY >= 0 && checkY < mapHeight) {
                            if (terrain[checkY][checkX] != 0) {  // Non-zero means occupied by a player or resource
                                validSpawn = false; // Found an occupied area, reject this spawn point
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
            System.out.println("Player " + i + " spawn point: (" + x + ", " + y + ")");

            placeResourcesNear(x, y, 3);
        }
        placeRandomResources(18);
    }


    // Place resources near a given location
    private void placeResourcesNear(int x, int y, int numberOfResources) {
        mapLock.lock();
        try {
            Random random = new Random();

            for (int i = 0; i < numberOfResources; i++) {
                int dx, dy;

                // Try to find a valid nearby location that doesn't overlap with a player
                do {
                    // Randomly choose a nearby location (within a 2-tile radius)
                    dx = Math.max(0, Math.min(mapWidth - 1, x + random.nextInt(5) - 2));
                    dy = Math.max(0, Math.min(mapHeight - 1, y + random.nextInt(5) - 2));
                } while (terrain[dy][dx] != 0);  // Repeat if the tile is occupied by a player or another resource

                // Randomly select a resource type
                String resourceType = resourceTypes[random.nextInt(resourceTypes.length)];

                // Create a new resource object
                Resource resource = new Resource(resourceType, dx, dy);
                resources.add(resource);

                // Mark the resource on the terrain matrix
                terrain[dy][dx] = -1;  // Use -1 to represent a resource on the map

                System.out.println("Placed resource: " + resource);
            }
        } finally {
            mapLock.unlock();  // Ensure the lock is always released
        }
    }

    private void placeRandomResources(int numberOfResources) {
        mapLock.lock();
        try {
            Random random = new Random();

            for (int i = 0; i < numberOfResources; i++) {
                // Randomly choose a location anywhere on the map
                int x = random.nextInt(mapWidth);
                int y = random.nextInt(mapHeight);

                // Check that there's no player or resource already in that spot
                if (terrain[y][x] == 0) {
                    // Randomly select a resource type
                    String resourceType = resourceTypes[random.nextInt(resourceTypes.length)];

                    // Create a new resource object
                    Resource resource = new Resource(resourceType, x, y);
                    resources.add(resource);

                    // Mark resources on the terrain matrix (optional)
                    terrain[y][x] = -1;  // Use -1 to represent a resource on the map

                    System.out.println("Placed random resource: " + resource);
                }
            }
        }finally {
            mapLock.unlock();  // Ensure the lock is always released
        }
    }
    public void printMap() {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (terrain[i][j] == 0) {
                    System.out.print(". ");  // Empty tile
                } else if (terrain[i][j] > 0) {
                    System.out.print("P" + terrain[i][j] + " ");  // Player spawn point
                } else {
                    System.out.print("R ");  // Resource
                }
            }
            System.out.println();
        }
    }

    public List<Resource> getResources() {
        return resources;
    }
}

// ToDo: Good synchronization across all thread requests