package com.migros_one.courier_tracking.store.service.intf;

import com.migros_one.courier_tracking.store.dto.StoreDTO;

import java.io.IOException;
import java.util.List;

public interface StoreLoaderService {
    public List<StoreDTO> readStores() throws IOException;
}
