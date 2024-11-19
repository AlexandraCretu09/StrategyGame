package com.example.StrategyGame.User;

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


    @PostMapping
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username){
        userRepository.deleteById(username);
    }

    @PostMapping("/command")
    public ResponseEntity<String> receiveUserCommand(@RequestBody User user) {
        userService.processUserCommand(user);
        return ResponseEntity.ok("User command received and processed.");
    }
}
