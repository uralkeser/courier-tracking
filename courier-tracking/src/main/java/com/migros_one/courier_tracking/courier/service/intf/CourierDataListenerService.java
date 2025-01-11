package com.migros_one.courier_tracking.courier.service.intf;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface CourierDataListenerService {

    void handleTextMessage(WebSocketSession session, TextMessage message);

    void handleCourierMessage(String message);

}
