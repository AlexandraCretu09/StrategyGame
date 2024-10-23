package org.example;

public class CommandsLogic {
    private String command;
    public CommandsLogic(String command){
        this.command = command;
    }
    public void processCommand(String userToken) {
        switch (this.command) {
            case "move":
                // Logic for move command
                System.out.println(userToken + " moved.");
                break;
            case "interactWithResource":
                System.out.println(userToken + " interacted with a resource.");
                break;
            case "build":
                System.out.println(userToken + " built something.");
                break;
            default:
                System.out.println("Invalid command: " + command);
                break;
        }
    }
}
