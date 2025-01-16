package com.migros_one.courier_tracking.courier.mapper;

import com.migros_one.courier_tracking.courier.dto.CourierDTO;

import com.migros_one.courier_tracking.courier.entity.CourierEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "SPRING")
public interface CourierMapper {

    CourierDTO entityToDTO(CourierEntity courierEntity);

    CourierEntity dtoToEntity(CourierDTO courierDTO);
}
