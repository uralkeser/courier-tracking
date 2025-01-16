package com.migros_one.courier_tracking.store.mapper;


import com.migros_one.courier_tracking.store.dto.StoreDTO;
import com.migros_one.courier_tracking.store.entity.StoreEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "SPRING")
public interface StoreMapper {

    StoreDTO entityToDTO(StoreEntity storeEntity);

    StoreEntity dtoToEntity(StoreDTO storeDTO);

    List<StoreEntity> dtoListToEntityList(List<StoreDTO> storeDTOList);

    List<StoreDTO> entityListToDtoList(List<StoreEntity> storeDTOList);

}
