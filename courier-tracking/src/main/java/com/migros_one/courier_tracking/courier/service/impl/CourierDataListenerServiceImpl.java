package com.migros_one.courier_tracking.courier.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros_one.courier_tracking.courier.dto.CourierDTO;
import com.migros_one.courier_tracking.courier.service.intf.CourierDataListenerService;
import com.migros_one.courier_tracking.store.dto.StoreDTO;

import com.migros_one.courier_tracking.store.service.impl.StoreLoaderServiceImpl;
import com.migros_one.courier_tracking.store.service.intf.StoreLoaderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierDataListenerServiceImpl extends TextWebSocketHandler {

    private final StoreLoaderService storeLoaderService;
    private static final Logger log = LoggerFactory.getLogger(CourierDataListenerServiceImpl.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        handleCourierMessage(payload);
        log.info("Received message: " + message.getPayload());
    }

    protected void handleCourierMessage(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CourierDTO courierDTO = objectMapper.readValue(message, CourierDTO.class);
            detectCouriersCloseToStores(courierDTO);
        } catch (JsonProcessingException jsonMappingException){
            log.error(jsonMappingException.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    protected void detectCouriersCloseToStores(CourierDTO courierDTO) throws IOException{
        List<StoreDTO> storeDTOList = storeLoaderService.readStores();
        for (StoreDTO storeDTO : storeDTOList) {
            double distance = calculateDistance(courierDTO.getLatitude(), courierDTO.getLongitude(), storeDTO.getLatitude(), storeDTO.getLongitude());
            if (distance <= 100) {
                log.info("Courier " + courierDTO.getCourierId() + " close to " + storeDTO.getName() + " with the distance "+ distance);
            }
        }
    }

    protected double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int EARTH_RADIUS = 6371; // Kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
