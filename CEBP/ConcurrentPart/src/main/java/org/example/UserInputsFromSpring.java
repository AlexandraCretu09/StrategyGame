package org.example;

import org.example.Exceptions.ComandQueueFullException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class UserInputsFromSpring {
    private String userToken;
    private BlockingQueue<String> commandQueue;
    private boolean active = true;

    public UserInputsFromSpring(String userToken){
        this.userToken = userToken;
        this.commandQueue = new LinkedBlockingDeque<>();
    }

    public void addCommand(String command){
        try {
            commandQueue.add(command);
        } catch (ComandQueueFullException e){
            System.out.println("Queue full.");
        }
    }

    public void run() {
        while (active) {
            try {
                CommandsLogic commandsLogic = new CommandsLogic(commandQueue.take());
                commandsLogic.processCommand(this.userToken);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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
