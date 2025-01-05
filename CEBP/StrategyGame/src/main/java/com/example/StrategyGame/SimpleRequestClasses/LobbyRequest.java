package com.example.StrategyGame.SimpleRequestClasses;

import java.util.List;

public class LobbyRequest {
    private List<String> usernamesList;
    private int lobbyId;

    public LobbyRequest(List<String> usernamesList, int lobbyId) {
        this.usernamesList = usernamesList;
        this.lobbyId = lobbyId;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public List<String> getUsernamesList() {
        return usernamesList;
    }

    public void setUsernamesList(List<String> usernamesList) {
        this.usernamesList = usernamesList;
    }
}
