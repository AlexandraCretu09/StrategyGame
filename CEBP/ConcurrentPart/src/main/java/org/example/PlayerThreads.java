package org.example;

import java.util.concurrent.BlockingQueue;

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

    public void moveOnTheMap(String command){


            switch (command) {
                case "moveUp" -> moveUp();
                case "moveDown" -> moveDown();
                case "moveLeft" -> moveLeft();
                case "moveRight" -> moveRight();
                case "end" -> stopMoving();
                default -> System.err.println("Invalid direction!");
            }

            gameMap.printMap();

            try{
                Thread.sleep(2000);
            }catch (InterruptedException e){
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
