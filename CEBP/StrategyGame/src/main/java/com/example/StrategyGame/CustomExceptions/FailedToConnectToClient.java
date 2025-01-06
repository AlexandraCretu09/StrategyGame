package com.example.StrategyGame.CustomExceptions;

public class FailedToConnectToClient extends RuntimeException{
    public FailedToConnectToClient(String message){ super(message); }
}
