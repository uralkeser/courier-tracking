package com.migros_one.courier_tracking.store.repository.custom;

import com.migros_one.courier_tracking.store.entity.StoreEntity;

public interface StoreCustomRepository {

    StoreEntity saveStore(StoreEntity storeEntity);
}
