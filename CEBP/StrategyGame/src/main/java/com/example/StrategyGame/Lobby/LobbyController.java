package com.example.StrategyGame.Lobby;


import com.example.StrategyGame.SimpleRequestClasses.LobbyParticipants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    @Autowired
    private LobbyRepository lobbyRepository;
    @Autowired
    private LobbyService lobbyService;

    @PostMapping
    public Lobby createLobby(@RequestBody Lobby lobby) { return lobbyRepository.save(lobby); }

    @PostMapping("/sendUsernames")
    public ResponseEntity<String> sendLobbyParticipants(@RequestBody LobbyParticipants lobbyParticipants){
        lobbyService.processUsernamesList(lobbyParticipants);
        return ResponseEntity.ok("User list received and processed.");
    }



}
