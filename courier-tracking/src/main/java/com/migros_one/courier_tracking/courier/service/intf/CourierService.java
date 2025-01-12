package com.migros_one.courier_tracking.courier.service.intf;


import com.migros_one.courier_tracking.courier.dto.CourierLocationDTO;
import com.migros_one.courier_tracking.store.dto.StoreDTO;

import java.util.List;

public interface CourierService {

    void checkCourierNearToStoreForUpdate(CourierLocationDTO courierLocationDTO, List<StoreDTO> storeDTOList);
}
