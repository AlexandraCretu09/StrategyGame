package org.example;

public class PlayerThreads implements Runnable {
    private Map gameMap;
    private int i, j;  // Current position of the player
    private int playerId;
    private boolean running = true;
    private int[] commandsList;

    public PlayerThreads(Map gameMap, int playerId, int startX, int startY, int[]commandsList) {
        this.gameMap = gameMap;
        this.playerId = playerId;
        int[] startingPosition = gameMap.getPlayerStartingPosition(playerId);
        this.i = startingPosition[0];
        this.j = startingPosition[1];
        this.commandsList = commandsList;

        //System.out.println("Player " + playerId + " initialized at position: (" + i + ", " + j + ")");


    }

    @Override
    public void run() {
        while (running) {
            moveOnTheMap(commandsList);
        }
    }

    public void moveLeft() {
        if (i > 0 && gameMap.movePlayer(playerId, i, j, i - 1, j)) {
            i -= 1;
        }else if (i <= 0)
            System.out.println("Position out of map bounds!");
    }

    public void moveRight() {
        if (i < gameMap.getHeight() - 1 && gameMap.movePlayer(playerId, i, j, i + 1, j)) {
            i += 1;
        } else if (i >= gameMap.getHeight())
            System.out.println("Position out of map bounds!");
    }

    public void moveUp() {
        if (j > 0 && gameMap.movePlayer(playerId, i, j, i, j - 1)) {
            j -= 1;
        }else if (j <= 0)
            System.out.println("Position out of map bounds!");
    }

    public void moveDown() {
        if (j < gameMap.getWidth() - 1 && gameMap.movePlayer(playerId, i, j, i, j + 1)) {
            j += 1;
        }else if (j >= gameMap.getHeight())
            System.out.println("Position out of map bounds!");
    }

    public void moveOnTheMap(int[]commands){

        for(int c: commands) {
            switch (c) {
                case 1 -> moveUp();
                case 2 -> moveDown();
                case 3 -> moveLeft();
                case 4 -> moveRight();
                case 5 -> stopMoving();
                default -> System.out.println("Invalid direction!");
            }

            gameMap.printMap();

            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void stopMoving() {
        running = false;
    }
}
