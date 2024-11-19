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

    @Column(nullable = false)
    private String command;

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;


    public long getID() { return this.ID; }
    public void setUsername(String username) { this.username = username; }

    public String getUsername() { return this.username;}

    public  void setCommand(String command){ this.command = command; }
    public String getCommand(){ return this.command; }

}
