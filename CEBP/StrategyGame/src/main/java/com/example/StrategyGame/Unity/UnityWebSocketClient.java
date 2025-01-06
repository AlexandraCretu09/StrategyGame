package com.example.StrategyGame.Unity;

import com.example.StrategyGame.Map.GameMap;
import com.example.StrategyGame.SimpleRequestClasses.Resources;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void sendGameMap(GameMap gameMap, List<Resources> resources) {
        if (this.isOpen()) {
            try {

                String jsonMap = objectMapper.writeValueAsString(gameMap);
                String jsonRes = objectMapper.writeValueAsString(resources);

                Map<String, String> payload = new HashMap<>();
                payload.put("gameMap", jsonMap);
                payload.put("resources", jsonRes);

                String jsonPayload = objectMapper.writeValueAsString(payload);

                this.send(jsonPayload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("WebSocket connection is not open. Cannot send data.");
        }
    }

}
