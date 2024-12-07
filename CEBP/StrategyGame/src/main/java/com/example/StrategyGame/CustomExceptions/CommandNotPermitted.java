package com.example.StrategyGame.CustomExceptions;

public class CommandNotPermitted extends RuntimeException{
    public CommandNotPermitted(String message){
        super(message);
    }
}
