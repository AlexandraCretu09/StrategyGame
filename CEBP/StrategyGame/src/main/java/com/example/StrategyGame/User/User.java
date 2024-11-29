package com.example.StrategyGame.User;

import com.example.StrategyGame.Lobby.Lobby;
import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;


    public long getID() { return this.ID; }
    public void setUsername(String username) { this.username = username; }

    public String getUsername() { return this.username;}

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}
