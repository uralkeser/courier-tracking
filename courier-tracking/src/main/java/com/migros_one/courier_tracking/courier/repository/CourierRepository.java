package com.migros_one.courier_tracking.courier.repository;

import com.migros_one.courier_tracking.courier.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<CourierEntity, Long> {
}
