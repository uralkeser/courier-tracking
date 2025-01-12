package com.migros_one.courier_tracking.store.repository;

import com.migros_one.courier_tracking.store.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, String> {

    Optional<StoreEntity> findByName(String name);
}
