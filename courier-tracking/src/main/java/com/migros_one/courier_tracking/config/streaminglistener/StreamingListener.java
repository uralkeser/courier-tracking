package com.migros_one.courier_tracking.config.streaminglistener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.concurrent.ExecutionException;

@Service
public class StreamingListener implements CommandLineRunner {

    private static final String senderUrl = "ws://localhost:9000";

    @Override
    public void run(String... args) {
        StandardWebSocketClient client = new StandardWebSocketClient();
        try {
            WebSocketSession session = client.execute(new CourierWebSocketHandler(), senderUrl).get();
            while (session.isOpen()) {
                // Keep the connection alive
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("WebSocket connection lost. Attempting to reconnect...");
        }
    }
}
