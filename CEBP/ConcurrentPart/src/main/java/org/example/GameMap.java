package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;


public class GameMap {

    private int[][] terrain;
    private Map<String, House> houses = new ConcurrentHashMap<>();
    // 0 for empty, -1 for resource, >0 for player ID
    private final Lock[][] cellLocks; // Locks for each cell to prevent concurrent access
    private static int noOfPlayers;
    private List<Resource> resources;
    private int mapWidth;
    private int mapHeight;

    private Map<Integer, User> players = new HashMap<>();

    // Resource types
    private static final String[] resourceTypes = {"wood", "stone", "gold"};
    private List<int[]> playerStartingPositions;
    private Set<String> playerPositions = new HashSet<>(); // Track occupied player positions

    private final ReentrantLock consoleLock = new ReentrantLock();

    private User getUserById(int playerId) {
        return players.get(playerId);
    }

    public User getUserByToken(String token) {
        return players.values().stream()
                .filter(user -> token.equals(user.getUserToken()))
                .findFirst()
                .orElse(null);
    }


    public GameMap(int rows, int cols, int noOfPlayers, List<User> users) {
        this.terrain = new int[rows][cols];
        this.cellLocks = new ReentrantLock[rows][cols]; // Create a lock for each cell
        this.mapHeight = rows;
        this.mapWidth = cols;
        GameMap.noOfPlayers = noOfPlayers;
        this.resources = new CopyOnWriteArrayList<>();
        playerStartingPositions = new ArrayList<>(noOfPlayers);

        // Initialize locks for each cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellLocks[i][j] = new ReentrantLock();
            }
        }

        initializeMap();

        this.players = new HashMap<>();
        for (User user : users) {
            this.players.put(user.getPlayerId(), user);
        }

    }

    // Add these methods in the Map class

    public int getHeight() {
        return mapHeight;
    }

    public int getWidth() {
        return mapWidth;
    }

    private void initializeMap() {
        // Base map size and scaling factor
        int baseSize = 10; // Minimum map size
        int scaleFactor = 2; // Growth factor for map size

        // Calculate dynamic map size based on the number of players
        int mapDimension = baseSize + Math.max(0, (noOfPlayers - 2) * scaleFactor);
        mapWidth = mapDimension;
        mapHeight = mapDimension;

        System.out.println("Map size: " + mapWidth + "x" + mapHeight);


        Random random = new Random();
        int bufferZone = 3; // Ensure players have space around them
        int outerMargin = 1;
        int innerRestrictedAreaMargin = Math.min(mapWidth, mapHeight) / 2;

        // Resize terrain and reset player positions
        terrain = new int[mapHeight][mapWidth];
        playerPositions.clear();
        playerStartingPositions.clear();

        // Define spawn and restricted area boundaries
        int spawnAreaStartX = outerMargin;
        int spawnAreaEndX = mapWidth - outerMargin - 1;
        int spawnAreaStartY = outerMargin;
        int spawnAreaEndY = mapHeight - outerMargin - 1;

        int restrictedAreaStartX = (mapWidth / 2) - innerRestrictedAreaMargin / 2;
        int restrictedAreaEndX = (mapWidth / 2) + innerRestrictedAreaMargin / 2;
        int restrictedAreaStartY = (mapHeight / 2) - innerRestrictedAreaMargin / 2;
        int restrictedAreaEndY = (mapHeight / 2) + innerRestrictedAreaMargin / 2;

        int maxAttempts = mapWidth * mapHeight * 4 / noOfPlayers;

        // Place each player at a random location
        for (int i = 1; i <= noOfPlayers; i++) {
            int x = 0, y = 0;
            boolean validSpawn = false;
            int attempts = 0;

            while (!validSpawn && attempts < maxAttempts) {
                attempts++;
                // Generate random coordinates within the spawn area
                x = random.nextInt(spawnAreaEndX - spawnAreaStartX + 1) + spawnAreaStartX;
                y = random.nextInt(spawnAreaEndY - spawnAreaStartY + 1) + spawnAreaStartY;

                // Skip restricted areas in the middle of the map
                if (x >= restrictedAreaStartX && x <= restrictedAreaEndX &&
                        y >= restrictedAreaStartY && y <= restrictedAreaEndY) {
                    continue;
                }

                validSpawn = true; // Assume position is valid until checked

                // Check buffer zone around the proposed position
                for (int dx = -bufferZone; dx <= bufferZone; dx++) {
                    for (int dy = -bufferZone; dy <= bufferZone; dy++) {
                        int checkX = x + dx;
                        int checkY = y + dy;

                        // Ensure position is within map bounds and check for other players
                        if (checkX >= 0 && checkX < mapWidth && checkY >= 0 && checkY < mapHeight) {
                            String checkPositionKey = checkX + "," + checkY;
                            if (playerPositions.contains(checkPositionKey)) {
                                validSpawn = false;
                                break;
                            }
                        }
                    }
                    if (!validSpawn) break;
                }
            }

            if (!validSpawn) {
                System.err.println("Unable to find a valid spawn location for player " + i + " after "
                        + attempts + " attempts.");
                continue;
            }

            // Place the player in the terrain and record their position
            terrain[y][x] = i;
            playerPositions.add(x + "," + y); // Add to player positions to track where players are
            System.out.println("Player " + i + " spawn point: (" + x + ", " + y + ")");
            playerStartingPositions.add(new int[]{x, y});

            // Place resources around this spawn point
            placeResourcesNear(x, y, 4);
        }

        // Place additional resources randomly across the map
        placeRandomResources(8 * noOfPlayers);
    }

    public int[] getPlayerStartingPosition(int playerId) {

        if (playerId - 1 < 0 || playerId - 1 >= playerStartingPositions.size()) {
            throw new IllegalArgumentException("Player ID out of range: " + playerId);
        }
        return playerStartingPositions.get(playerId - 1);
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
            //System.out.println("Placed resource: " + resource);
            System.out.println("Placed resource: " + resource.getType() + " at (" + resource.getX() + ", " + resource.getY() + ")");

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
                //System.out.println("Placed random resource: " + resource);
            }
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
        String newPositionKey = newX + "," + newY;

        // Check for out-of-bounds move
        if (newX < 0 || newX >= mapWidth || newY < 0 || newY >= mapHeight) {
            System.out.println("Move out of bounds!");
            return false;
        }

        Lock newCellLock = cellLocks[newY][newX];
        newCellLock.lock();
        try {
            // Check if the new position is occupied by another player
            if (playerPositions.contains(newPositionKey) && !(oldX == newX && oldY == newY)) {
                consoleLock.lock();
                try {
                    System.out.println("Position occupied by another player!");
                } finally {
                    consoleLock.unlock();
                }
                return false; // Block move if another player is in the target cell
            }

            for (Resource r : resources) {
                if ((r.getX() == newX && r.getY() == newY) && !(oldX == newX && oldY == newY)) {
                    consoleLock.lock();
                    try {
                        System.out.println("Position occupied by a resource!");
                    } finally {
                        consoleLock.unlock();
                    }
                    return false;
                }
            }

            // Move is safe; update position on the terrain and in player positions
            terrain[newY][newX] = playerId; // Set new position on the terrain

            consoleLock.lock();
            try {
                System.out.println("Moving player " + playerId + " from (" + oldX + ", " + oldY + ") to (" + newX + ", " + newY + ")");
            } finally {
                consoleLock.unlock();
            }

            if (oldX != -1 && oldY != -1) { // If there is a valid old position
                Lock oldCellLock = cellLocks[oldY][oldX];
                oldCellLock.lock();
                try {
                    // Clear the old position on the terrain and update player positions
                    terrain[oldY][oldX] = 0; // Reset old position on the terrain
                    playerPositions.remove(oldX + "," + oldY); // Remove old position from playerPositions
                } finally {
                    oldCellLock.unlock();
                }
            }

            // Update the new position in the player positions
            playerPositions.add(newPositionKey);
            return true; // Move successful
        } finally {
            newCellLock.unlock();
        }
    }

    public synchronized void printMap() {
        consoleLock.lock();
        try {
            for (int i = 0; i < mapHeight; i++) {
                for (int j = 0; j < mapWidth; j++) {
                    boolean resourceFound = false;

                    // Check if there's a resource at this location
                    for (Resource resource : resources) {
                        if (resource.getX() == j && resource.getY() == i) {
                            String resourceSymbol = switch (resource.getType()) {
                                case "wood" -> "w";
                                case "stone" -> "s";
                                case "gold" -> "g";
                                default -> "R"; // Default for unknown resource type
                            };

                            // Check if a house is placed at this resource
                            String positionKey = j + "," + i;
                            if (houses.containsKey(positionKey)) {
                                System.out.print(resourceSymbol + "* "); // Add * if house exists
                            } else {
                                System.out.print(resourceSymbol + " "); // Otherwise, just print the resource
                            }

                            resourceFound = true;
                            break;
                        }
                    }

                    // If no resource is found, print terrain or player
                    if (!resourceFound) {
                        if (terrain[i][j] == 0) {
                            System.out.print(". "); // Empty space
                        } else if (terrain[i][j] > 0) {
                            System.out.print("P" + terrain[i][j] + " "); // Player ID
                        } else {
                            System.out.print(". "); // Fallback for unexpected cases
                        }
                    }
                }
                System.out.println();
            }
            System.out.println();
        } finally {
            consoleLock.unlock();
        }
    }
    public String terrainToJSON() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.terrain);
    }




    public synchronized boolean placeHouse(int playerId, int x, int y, String resourceType) {
        String positionKey = x + "," + y;

        if (houses.containsKey(positionKey)) {
            System.out.println("House already exists at this location!");
            return false;
        }

        // Ensure the position is on a resource

        System.out.printf("Checking terrain and resources at (%d, %d).%n", x, y);
        resources.forEach(r -> System.out.printf("Resource at (%d, %d): %s%n", r.getX(), r.getY(), r.getType()));
        System.out.println("Terrain at this position: " + terrain[y][x]);
        Resource resource = resources.stream()
                .filter(r -> r.getX() == x && r.getY() == y && r.getType().equalsIgnoreCase(resourceType))
                .findFirst()
                .orElse(null);
        if (resource == null) {
            System.out.println("Invalid resource for placing a house!");
            return false;
        }

        // Get the user
        User user = getUserById(playerId);
        if (user == null) {
            System.err.println("Player not found!");
            return false;
        }

        // Check resource costs
        int woodCost = 70;
        int stoneCost = 10;
        if (user.getWood() < woodCost || user.getStone() < stoneCost) {
            System.out.println("Not enough resources to build the house!");
            return false;
        }

        // Deduct resources
        user.setWood(user.getWood() - woodCost);
        user.setStone(user.getStone() - stoneCost);

        // Place the house
        House house = new House(resourceType, switch (resourceType) {
            case "wood" -> 50;
            case "stone" -> 25;
            case "gold" -> 15;
            default -> 0;
        });
        houses.put(positionKey, house);
        System.out.println("House placed by player " + playerId + " at (" + x + ", " + y + ").");

        return true;
    }


    public synchronized boolean destroyHouse(int playerId, int x, int y) {
        String positionKey = x + "," + y;

        if (!houses.containsKey(positionKey)) {
            System.out.println("No house exists at this location!");
            return false;
        }

        House house = houses.get(positionKey);

        // Remove the house
        houses.remove(positionKey);
        System.out.println("House at (" + x + ", " + y + ") destroyed by player " + playerId + ".");

        return true;
    }

    public synchronized boolean upgradeHouse(int playerId, int x, int y) {
        String positionKey = x + "," + y;

        if (!houses.containsKey(positionKey)) {
            System.out.println("No house exists at this location!");
            return false;
        }

        House house = houses.get(positionKey);
        User user = getUserById(playerId);
        if (user == null) {
            System.err.println("Player not found!");
            return false;
        }

        // Upgrade costs
        int woodCost = 70;
        int stoneCost = 10;
        int goldCost = 5;

        // Check resources
        if (user.getWood() < woodCost || user.getStone() < stoneCost || user.getGold() < goldCost) {
            System.out.println("Not enough resources to upgrade the house!");
            return false;
        }

        // Deduct resources
        user.setWood(user.getWood() - woodCost);
        user.setStone(user.getStone() - stoneCost);
        user.setGold(user.getGold() - goldCost);

        // Upgrade the house
        house.setProductionRate(house.getProductionRate() + 10); // Increase production rate
        System.out.println("House at (" + x + ", " + y + ") upgraded by player " + playerId + ".");
        return true;
    }

}
