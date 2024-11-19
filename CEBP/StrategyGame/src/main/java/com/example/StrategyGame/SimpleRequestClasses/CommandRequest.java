package com.example.StrategyGame.SimpleRequestClasses;

public class CommandRequest {
    private String username;
    private String command;

    public CommandRequest(String username, String command){
        this.username = username;
        this.command = command;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
