package com.migros_one.courier_tracking.courier.service.intf;


import com.migros_one.courier_tracking.courier.dto.CourierDTO;


public interface CourierService {

    void logCourierNearToStore(CourierDTO newCourierDTO) throws Exception;

    void updateTotalDistance(CourierDTO newCourierDTO) throws Exception;

    Double getTotalTravelDistance(Long courierId) throws Exception;

    void updateCourier(CourierDTO newCourierDTO);
}
