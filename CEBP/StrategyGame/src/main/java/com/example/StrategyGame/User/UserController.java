package com.example.StrategyGame.User;

import com.example.StrategyGame.SimpleRequestClasses.SimpleUser;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Operation(summary = "Creates a User", description = "Saves the user in the database.")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @Operation(summary = "Deletes a user based on username")
    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userRepository.deleteById(username);
    }

    @Operation(summary = "Processes the username and the command", description = "Sends the received username and command to the concurrent part")
    @PostMapping("/command")
    public ResponseEntity<String> receiveUserCommand(@RequestBody SimpleUser user) {
        userService.processUserCommand(user);
        return ResponseEntity.ok("User command received and processed.");
    }

    @Operation(summary = "Processes the username and the command in the URL", description = "Sends the received username and command to the concurrent part")
    @PostMapping("/usernameAndCommand")
    public ResponseEntity<String> receiveUserCommandInURL(@RequestParam String username, @RequestParam String command) {
        userService.processUserCommandSeparately(username, command);
        return ResponseEntity.ok("User command received and processed.");
    }

    @Operation(summary = "Lists all players in the lobby")
    @GetMapping("/lobbyParticipants")
    public List<String> getUsernamesByLobbyId(@RequestParam Integer lobbyId) {
        return userService.getUsernamesByLobbyId(lobbyId);
    }

}

