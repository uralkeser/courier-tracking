package com.migros_one.courier_tracking.config.streaminglistener;

import com.migros_one.courier_tracking.courier.service.impl.CourierDataListenerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class StreamingListener implements CommandLineRunner {

    private static final String senderUrl = "ws://localhost:9000";
    private final CourierDataListenerServiceImpl courierDataListenerService;

    @Override
    public void run(String... args) {
        StandardWebSocketClient client = new StandardWebSocketClient();
        try {
            WebSocketSession session = client.execute(courierDataListenerService, senderUrl).get();
            while (session.isOpen()) {
                // Keep the connection alive
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("WebSocket connection lost. Attempting to reconnect...");
        }
    }
}
