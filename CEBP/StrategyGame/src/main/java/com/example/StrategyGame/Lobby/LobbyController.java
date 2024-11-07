package com.example.StrategyGame.Lobby;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    @Autowired
    private LobbyRepository lobbyRepository;

    @PostMapping
    public Lobby createLobby(@RequestBody Lobby lobby) { return lobbyRepository.save(lobby); }



}
