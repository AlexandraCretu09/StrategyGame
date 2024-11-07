package com.example.StrategyGame.Lobby;

import com.example.StrategyGame.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.List;

public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lobbyId;
    @Column(nullable = false)
    private int noOfPlayers;
    @Column(nullable = false)
    private Date creationDate;
    @Column(nullable = false)
    private String gameDuration;
    @Column
    private List<User> usersList;



    public int getLobbyId() {
        return lobbyId;
    }


    public int getNoOfPlayers() {
        return noOfPlayers;
    }

    public void setNoOfPlayers(int noOfPlayers) {
        this.noOfPlayers = noOfPlayers;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(String gameDuration) {
        this.gameDuration = gameDuration;
    }



    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }
}
