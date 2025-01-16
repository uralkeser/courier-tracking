package com.migros_one.courier_tracking.courier.repository;

import com.migros_one.courier_tracking.courier.entity.CourierTotalDistanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierTotalDistanceRepository extends JpaRepository<CourierTotalDistanceEntity, Long> {

    Optional<CourierTotalDistanceEntity> findByCourier_Id(Long courierId);
}
