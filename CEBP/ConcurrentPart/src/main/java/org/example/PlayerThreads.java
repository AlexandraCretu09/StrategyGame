package main.java.org.example;

public class PlayerThreads implements Runnable {
    private Map gameMap;
    private int i, j;  // Current position of the player
    private int playerId;
    private boolean running = true;

    public PlayerThreads(Map gameMap, int playerId, int startX, int startY) {
        this.gameMap = gameMap;
        this.playerId = playerId;
        int[] startingPosition = gameMap.getPlayerStartingPosition(playerId);
        this.i = startingPosition[0];
        this.j = startingPosition[1];

        // Move player to starting position if valid
        if (gameMap.movePlayer(playerId, -1, -1, i, j)) {
            System.out.println("Player " + playerId + " initialized at position: (" + i + ", " + j + ")");
        } else {
            System.err.println("Failed to initialize player " + playerId + " at position: (" + i + ", " + j + ")");
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(500); // Game loop sleep time, adjust as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveLeft() {
        if (i > 0 && gameMap.movePlayer(playerId, i, j, i - 1, j)) {
            i -= 1;
        }
    }

    public void moveRight() {
        if (i < gameMap.getHeight() - 1 && gameMap.movePlayer(playerId, i, j, i + 1, j)) {
            i += 1;
        }
    }

    public void moveUp() {
        if (j > 0 && gameMap.movePlayer(playerId, i, j, i, j - 1)) {
            j -= 1;
        }
    }

    public void moveDown() {
        if (j < gameMap.getWidth() - 1 && gameMap.movePlayer(playerId, i, j, i, j + 1)) {
            j += 1;
        }
    }

    public void stopMoving() {
        running = false;
    }
}
