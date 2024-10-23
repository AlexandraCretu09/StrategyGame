package com.example.StrategyGame.User;

import org.springframework.web.client.RestTemplate;

public class UserService {
    private static final String ConcurrentProjectURL = "http://localhost:8081/api/commands";

    public void processUserCommand(User user) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(ConcurrentProjectURL, user, String.class);
    }

}
