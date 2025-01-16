package com.migros_one.courier_tracking.store.service.impl;

import com.migros_one.courier_tracking.store.dto.StoreDTO;
import com.migros_one.courier_tracking.store.entity.StoreEntity;
import com.migros_one.courier_tracking.store.mapper.StoreMapper;
import com.migros_one.courier_tracking.store.repository.StoreRepository;
import com.migros_one.courier_tracking.store.service.intf.StoreLoaderService;
import com.migros_one.courier_tracking.store.service.intf.StoreService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private static final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);
    private final StoreLoaderService storeLoaderService;
    private final StoreRepository repository;
    private final StoreMapper mapper;
    private boolean isStoreLoaded = false;
    private List<StoreDTO> storeDTOList = List.of();

    @Transactional
    public List<StoreDTO> loadStores() throws IOException {
        if(!isStoreLoaded){
            storeDTOList = storeLoaderService.readStores();
            saveAllStores(storeDTOList);
            isStoreLoaded = true;
        }
        return storeDTOList;
    }

    @Transactional
    public void saveAllStores(List<StoreDTO> readStore) {
        if (readStore == null || readStore.isEmpty()) {
            log.warn("No store data found in the JSON file.");
            return;
        }
        try {
            readStore.forEach(newStore -> {
                    Optional<StoreEntity> optionalStore = repository.findByName(newStore.getName());
                    optionalStore.ifPresentOrElse((presentStore -> {
                                presentStore.setLatitude(newStore.getLatitude());
                                presentStore.setLongitude(newStore.getLongitude());
                                repository.saveStore(presentStore);
                            }),
                    () -> repository.saveStore(mapper.dtoToEntity(newStore)));
            });

        }
        catch (Exception e) {
            log.error("Error processing store: ", e);
        }
    }

    @Override
    public Optional<StoreEntity> getByName(String storeName) {
        return repository.findByName(storeName);
    }


}
