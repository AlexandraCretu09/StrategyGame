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
    public void createLobby(@RequestBody Lobby lobby) {
        lobbyRepository.save(lobby);
    }

    @PostMapping("/sendUsernames")
    public ResponseEntity<String> sendLobbyParticipants(@RequestBody LobbyParticipants lobbyParticipants){

        Lobby lobby = lobbyService.registerLobby(lobbyParticipants);
        createLobby(lobby);

        lobbyService.processUsernamesList(lobbyParticipants);
        return ResponseEntity.ok("User list received and processed.");
    }



}
