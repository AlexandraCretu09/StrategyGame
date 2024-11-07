package org.example;

public class User {

    private int playerId;
    private String username;

    public User(int playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
