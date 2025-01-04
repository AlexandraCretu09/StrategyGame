package com.example.StrategyGame.Lobby;

import com.example.StrategyGame.CustomExceptions.CommandNotPermitted;
import com.example.StrategyGame.CustomExceptions.LobbyNotFoundException;
import com.example.StrategyGame.SimpleRequestClasses.LobbyParticipants;
import com.example.StrategyGame.User.User;
import com.example.StrategyGame.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Service
public class LobbyService {
    @Autowired
    private LobbyRepository lobbyRepository;
    @Autowired
    private UserService userService;

    private static final String ConcurrentProjectURL = "http://concurrentpartcontainer:8081/api/usernames";

    private void processUsernamesList(List<String> usernamesList){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> request = new HttpEntity<>(usernamesList, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(ConcurrentProjectURL, request, String.class);
    }


    private Lobby findLobbyById(int lobbyId) {
        return lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new LobbyNotFoundException("Lobby not found with ID: " + lobbyId));
    }

    public int addFirstPlayerToLobby(String ipAddress, String username){
        Lobby lobby = new Lobby();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        lobby.setCreationDate(LocalDateTime.now());
        lobby.setNoOfPlayers(1);

        User user = userService.createUser(username, lobby);
        List<User> usersList = new ArrayList<>();
        usersList.add(user);
        lobby.setUsersList(usersList);

        lobby.setIpList(new ArrayList<>());
        lobby.getIpList().add(ipAddress);

        lobby.setGameDuration("");
        lobby.setJoinable(true);

        lobby.setCreatorUsername(username);
        lobbyRepository.save(lobby);

        return lobby.getLobbyId();
    }

    public String addPlayerToLobby(int lobbyId, String ipAddress, String username) {
        Lobby lobby = findLobbyById(lobbyId);

        if (!lobby.isJoinable()) {
            throw new IllegalStateException("Lobby is not joinable at the moment.");
        }

        if (!lobby.getIpList().contains(ipAddress)) {
            lobby.getIpList().add(ipAddress);

            User user = userService.createUser(username, lobby);

            lobby.getUsersList().add(user);
            lobby.setNoOfPlayers(lobby.getNoOfPlayers() + 1);
            lobbyRepository.save(lobby);
        }
        return ipAddress;
    }

    public void startGame(String username, int lobbyId){
        Lobby lobby = findLobbyById(lobbyId);

        if(!lobby.getCreatorUsername().equals(username))
            throw new CommandNotPermitted("User doens't have enough permissions");
        if(!lobby.isJoinable())
            throw new CommandNotPermitted("Game cannot be started");
        lobby.setJoinable(false);
//        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        lobby.setGameCreationDate(LocalDateTime.now());
        lobbyRepository.save(lobby);

        List<String> usernames = userService.getUsernamesList(lobby.getUsersList());
        processUsernamesList(usernames);

    }

    public void notifyUnityToRefreshUsernames() {
        String unityUrl = "http://localhost:8084/refreshUsernames/";

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForObject(unityUrl, null, String.class);
            System.out.println("Notified Unity to refresh usernames.");
        } catch (Exception e) {
            System.out.println("Error notifying Unity: " + e.getMessage());
        }
    }


}
