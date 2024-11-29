package com.example.StrategyGame.Unity;

import com.example.StrategyGame.Map.GameMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class UnityWebSocketClient extends WebSocketClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public UnityWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connection opened to Unity WebSocket server.");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received from Unity: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed to Unity WebSocket server.");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void sendGameMap(GameMap gameMap) {
        if (this.isOpen()) {
            try {
                String json = objectMapper.writeValueAsString(gameMap);  // Convert GameMap to JSON string
                this.send(json);  // Send the JSON string
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("WebSocket connection is not open. Cannot send data.");
        }
    }

}
