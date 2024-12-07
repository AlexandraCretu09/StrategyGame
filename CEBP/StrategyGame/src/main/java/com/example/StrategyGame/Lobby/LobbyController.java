package com.example.StrategyGame.Lobby;


import com.example.StrategyGame.CustomExceptions.CommandNotPermitted;
import com.example.StrategyGame.CustomExceptions.LobbyNotFoundException;
import com.example.StrategyGame.SimpleRequestClasses.LobbyParticipants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    @Autowired
    private LobbyRepository lobbyRepository;
    @Autowired
    private LobbyService lobbyService;

//    @Operation(summary = "Sends a list of usernames", description = "Saves the list of usernames in the database and sends them to the concurrent part")
//    @PostMapping("/sendUsernames")
//    public ResponseEntity<String> sendLobbyParticipants(@RequestBody LobbyParticipants lobbyParticipants){
//
//        Lobby lobby = lobbyService.registerLobby(lobbyParticipants);
//        createLobby(lobby);
//
//        lobbyService.processUsernamesList(lobbyParticipants);
//        return ResponseEntity.ok("User list received and processed.");
//    }

    @Operation(summary = "Creates a lobby")
    @PostMapping("/createLobby")
    public ResponseEntity<String> createLobby(@RequestParam String ipAddress, @RequestParam String username){
        int lobbyId = lobbyService.addFirstPlayerToLobby(ipAddress, username);
        return ResponseEntity.ok(username + " Successfully created a lobby with the id: " + lobbyId);

    }

    @Operation(summary="Joins a lobby from the frontend")
    @PostMapping("/joinLobby")
    public ResponseEntity<String> joinLobby(@RequestParam int lobbyId, @RequestParam String ipAddress, @RequestParam String username){
        try {
            String registeredIp = lobbyService.addPlayerToLobby(lobbyId, ipAddress, username);
            return ResponseEntity.ok("IP Address registered: " + registeredIp);
        } catch (LobbyNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(summary = "Starts the game", description = "Marks joineable as false, sends command to the concurrent part to instantate a new game map")
    @PostMapping("/startGame")
    public ResponseEntity<String> startGame(@RequestParam String username, @RequestParam int lobbyId){
        try{
            lobbyService.startGame(username, lobbyId);
            return ResponseEntity.ok("Game started successfully");
        } catch (LobbyNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (CommandNotPermitted ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }



}
