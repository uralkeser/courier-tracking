package com.migros_one.courier_tracking.courier.mapper;

import com.migros_one.courier_tracking.courier.dto.CourierLastStoreEntranceDTO;
import com.migros_one.courier_tracking.courier.entity.CourierLastStoreEntranceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "SPRING")
public interface CourierLastStoreEntranceMapper {

    CourierLastStoreEntranceDTO entityToDTO(CourierLastStoreEntranceEntity entity);

    CourierLastStoreEntranceEntity dtoToEntity(CourierLastStoreEntranceDTO dto);
}
