package com.migros_one.courier_tracking.courier.dto;

import com.migros_one.courier_tracking.courier.entity.CourierEntity;
import com.migros_one.courier_tracking.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourierLastStoreEntranceDTO {

    private Long id;
    private CourierEntity courier;
    private StoreEntity store;
    private LocalDateTime lastEntrance;
}
