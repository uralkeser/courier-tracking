package com.migros_one.courier_tracking.courier.repository;


import com.migros_one.courier_tracking.courier.entity.CourierLastStoreEntranceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CourierLastStoreEntranceRepository extends JpaRepository<CourierLastStoreEntranceEntity, Long> {

    @Query("SELECT e FROM CourierLastStoreEntranceEntity e WHERE e.courier.id = :courierId AND e.store.name = :storeName AND e.lastEntrance > :thresholdTime")
    Optional<CourierLastStoreEntranceEntity> findRecentEntrance(
            @Param("courierId") Long courierId,
            @Param("storeName") String storeName,
            @Param("thresholdTime") LocalDateTime thresholdTime
    );

    Optional<CourierLastStoreEntranceEntity> findByCourier_IdAndStore_Name(Long courierId, String storeName);

}
