package com.migros_one.courier_tracking.courier.repository;


import com.migros_one.courier_tracking.courier.entity.CourierStoreEntranceLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CourierStoreEntranceLogRepository extends JpaRepository<CourierStoreEntranceLogEntity, Long> {

    @Query("SELECT e FROM CourierStoreEntranceLogEntity e WHERE e.courier.id = :courierId AND e.store.name = :storeName AND e.entrance > :thresholdTime")
    Optional<CourierStoreEntranceLogEntity> findRecentEntrance(
            @Param("courierId") Long courierId,
            @Param("storeName") String storeName,
            @Param("thresholdTime") LocalDateTime thresholdTime
    );

}
