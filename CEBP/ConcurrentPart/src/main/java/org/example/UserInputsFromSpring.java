package org.example;

import org.example.Exceptions.ComandQueueFullException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class UserInputsFromSpring {
    private String userToken;
    private BlockingQueue<String> commandQueue;
    private boolean active = true;
    private GameMap gameMap;

    public UserInputsFromSpring(String userToken, GameMap gameMap) {
        this.userToken = userToken;
        this.commandQueue = new LinkedBlockingDeque<>();
        this.gameMap = gameMap;
    }

    public void addCommand(String command) {
        if (!commandQueue.offer(command)) {
            throw new ComandQueueFullException("Command queue is full for user: " + userToken);
        }
    }

    public void run() {
        while (active) {
            try {
                CommandsLogic commandsLogic = new CommandsLogic(commandQueue.take(), gameMap, userToken);
                commandsLogic.processCommand(); // Fixed the call to match no-argument method
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
            }
        }
    }

    public void stop() {
        active = false; // Stop the thread gracefully
    }

    public String getUserToken() {
        return userToken;
    }
}
