package com.example.StrategyGame.Lobby;

import com.example.StrategyGame.User.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lobbyId;
    @Column(nullable = false)
    private int noOfPlayers;
    @Column(nullable = false)
    private LocalDateTime creationDate;
    @Column(nullable = true)
    private LocalDateTime gameCreationDate;
    @Column(nullable = false)
    private String gameDuration;
    @Column
    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> usersList;
    @ElementCollection
    @CollectionTable(name = "lobby_ips", joinColumns = @JoinColumn(name = "lobby_id"))
    @Column(name = "ip_address")
    private List<String> ipList = new ArrayList<>();
    @Column(nullable = false)
    private boolean joinable;

    @Column(nullable = false)
    private String creatorUsername;



    public int getLobbyId() {
        return lobbyId;
    }


    public int getNoOfPlayers() {
        return noOfPlayers;
    }

    public void setNoOfPlayers(int noOfPlayers) {
        this.noOfPlayers = noOfPlayers;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
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

    public List<String> getIpList() {
        return ipList;
    }

    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }

    public boolean isJoinable() {
        return joinable;
    }

    public void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }
    public LocalDateTime getGameCreationDate() {
        return gameCreationDate;
    }

    public void setGameCreationDate(LocalDateTime gameCreationDate) {
        this.gameCreationDate = gameCreationDate;
    }
}
