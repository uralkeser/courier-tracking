package com.migros_one.courier_tracking.store.repository.custom;

import com.migros_one.courier_tracking.store.dto.StoreDTO;
import com.migros_one.courier_tracking.store.entity.StoreEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StoreCustomRepositoryImpl implements StoreCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public StoreEntity saveStore(StoreEntity storeEntity) {
        return entityManager.merge(storeEntity);
    }
}
