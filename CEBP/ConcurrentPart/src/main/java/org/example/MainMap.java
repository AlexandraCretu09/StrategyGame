package main.java.org.example;

public class MainMap {
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
    }
}
