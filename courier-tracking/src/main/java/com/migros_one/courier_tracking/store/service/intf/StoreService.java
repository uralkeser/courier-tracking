package com.migros_one.courier_tracking.store.service.intf;

import com.migros_one.courier_tracking.store.dto.StoreDTO;
import com.migros_one.courier_tracking.store.entity.StoreEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StoreService {

    Optional<StoreEntity> getByName(String storeName);

    List<StoreDTO> loadStores() throws IOException;

}
