package com.migros_one.courier_tracking.store.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migros_one.courier_tracking.store.dto.StoreDTO;
import com.migros_one.courier_tracking.store.service.intf.StoreLoaderService;
import com.migros_one.courier_tracking.store.service.intf.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StoreLoaderServiceImpl implements StoreLoaderService {

    private static final String PATH = "static/stores.json";

    public List<StoreDTO> readStores() throws IOException {
        ClassPathResource resource = new ClassPathResource(PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        return Arrays.asList(objectMapper.readValue(resource.getInputStream(), StoreDTO[].class));
    }
}
