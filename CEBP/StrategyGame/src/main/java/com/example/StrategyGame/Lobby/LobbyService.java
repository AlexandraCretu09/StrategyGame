package com.example.StrategyGame.Lobby;

import com.example.StrategyGame.SimpleRequestClasses.LobbyParticipants;
import com.example.StrategyGame.User.User;
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
    private static final String ConcurrentProjectURL = "http://localhost:8081/api/usernames";

    public void processUsernamesList(LobbyParticipants lobbyParticipants){

        List<String> usernamesList = new ArrayList<>();
        usernamesList = lobbyParticipants.getUsernames();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> request = new HttpEntity<>(usernamesList, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(ConcurrentProjectURL, request, String.class);
    }

    public Lobby registerLobby(LobbyParticipants lobbyParticipants){

        Lobby lobby = new Lobby();

        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        lobby.setCreationDate(LocalDateTime.now());

        List<String> usernames = new ArrayList<>();
        usernames = lobbyParticipants.getUsernames();
        lobby.setNoOfPlayers(usernames.size());

        List<User> usersList = new ArrayList<>();
        for(String u : usernames){
            User user = new User();
            user.setUsername(u);
            user.setLobby(lobby);
            usersList.add(user);
        }
        lobby.setUsersList(usersList);

        lobby.setGameDuration("");

        return lobby;

    }
}
