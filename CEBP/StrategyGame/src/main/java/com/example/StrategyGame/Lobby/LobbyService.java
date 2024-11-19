package com.example.StrategyGame.Lobby;

import com.example.StrategyGame.SimpleRequestClasses.LobbyParticipants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
}
