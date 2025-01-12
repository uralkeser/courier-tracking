package com.migros_one.courier_tracking.courier.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.migros_one.courier_tracking.courier.dto.CourierLocationDTO;
import com.migros_one.courier_tracking.courier.service.intf.CourierService;
import com.migros_one.courier_tracking.store.dto.StoreDTO;

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

    private static final Logger log = LoggerFactory.getLogger(CourierDataListenerServiceImpl.class);
    private final StoreLoaderService storeLoaderService;
    private final CourierService courierService;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        handleCourierMessage(payload);
        log.info("Received message: " + message.getPayload());
    }

    protected void handleCourierMessage(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            CourierLocationDTO courierLocationDTO = objectMapper.readValue(message, CourierLocationDTO.class);
            checkCourierNearToStores(courierLocationDTO);
        } catch (JsonProcessingException jsonMappingException){
            log.error("Cannot parse listening message for the reason: "+jsonMappingException.getMessage());
        }
    }

    protected void checkCourierNearToStores(CourierLocationDTO courierLocationDTO) {
        try {
            List<StoreDTO> storeDTOList = storeLoaderService.readStores();
            courierService.checkCourierNearToStoreForUpdate(courierLocationDTO, storeDTOList);
        } catch (IOException e) {
            log.error("Cannot read stores from json for the reason: "+e.getMessage());
        }
    }


}
