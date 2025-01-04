package com.example.StrategyGame.User;

import com.example.StrategyGame.Lobby.Lobby;
import com.example.StrategyGame.SimpleRequestClasses.CommandRequest;
import com.example.StrategyGame.SimpleRequestClasses.SimpleUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final String ConcurrentProjectURL = "http://concurrentpartcontainer:8081/api/commands";


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void processUserCommand(SimpleUser user) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        CommandRequest commandRequest = new CommandRequest(user.getUsername(), user.getCommand());
        HttpEntity<CommandRequest> request = new HttpEntity<>(commandRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ConcurrentProjectURL, request, String.class);

        System.out.println("Response from Java project: " + response.getBody());

    }

    public void processUserCommandSeparately(String username, String command) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        CommandRequest commandRequest = new CommandRequest(username, command);
        HttpEntity<CommandRequest> request = new HttpEntity<>(commandRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ConcurrentProjectURL, request, String.class);

        System.out.println("Response from Java project: " + response.getBody());

    }

    public User createUser(String username, Lobby lobby){
        User user = new User();
        user.setUsername(username);
        user.setLobby(lobby);
        return user;
    }
    public List<String> getUsernamesList(List<User> userList){
        List<String> usernames= new ArrayList<>();
        for(User u : userList){
            usernames.add(u.getUsername());
        }
        return usernames;
    }

    public List<String> getUsernamesByLobbyId(Integer lobbyId) {
        List<User> users = userRepository.findByLobby_LobbyId(lobbyId);
        return getUsernamesList(users);
    }

}
