package com.migros_one.courier_tracking.store.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros_one.courier_tracking.store.dto.StoreDTO;
import com.migros_one.courier_tracking.store.service.intf.StoreLoaderService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class StoreLoaderServiceImpl implements StoreLoaderService {

    private static final String PATH = "static/stores.json";

    public List<StoreDTO> readStores() throws IOException {
        ClassPathResource resource = new ClassPathResource(PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        return Arrays.asList(objectMapper.readValue(resource.getInputStream(), StoreDTO[].class));
    }
}
