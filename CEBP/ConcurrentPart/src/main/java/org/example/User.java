package org.example;

import java.util.UUID;

public class User {
    private int playerId;
    private String username;
    private String userToken; // Added field for user token

    private int wood = 100;
    private int stone = 50;
    private int gold = 25;

    private int positionX;
    private int positionY;

    // Constructor
    public User(int playerId, String username, String userToken) {
        this.playerId = playerId;
        this.username = username;
        this.userToken = username + "-" + UUID.randomUUID().toString();
    }


    // Getters and setters
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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
