package com.example.StrategyGame.User;

import com.example.StrategyGame.SimpleRequestClasses.CommandRequest;
import com.example.StrategyGame.SimpleRequestClasses.SimpleUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
    private static final String ConcurrentProjectURL = "http://localhost:8081/api/commands";

    public void processUserCommand(SimpleUser user) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        CommandRequest commandRequest = new CommandRequest(user.getUsername(), user.getCommand());
        HttpEntity<CommandRequest> request = new HttpEntity<>(commandRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ConcurrentProjectURL, request, String.class);

        System.out.println("Response from Java project: " + response.getBody());

    }

}
