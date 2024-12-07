package com.example.StrategyGame.CustomExceptions;

public class LobbyNotFoundException extends RuntimeException {
    public LobbyNotFoundException(String message) {
        super(message);
    }
}
