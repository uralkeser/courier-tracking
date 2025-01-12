package com.migros_one.courier_tracking.courier.service.impl;

import com.migros_one.courier_tracking.courier.dto.CourierLastStoreEntranceDTO;
import com.migros_one.courier_tracking.courier.dto.CourierLocationDTO;
import com.migros_one.courier_tracking.courier.entity.CourierEntity;
import com.migros_one.courier_tracking.courier.entity.CourierLastStoreEntranceEntity;
import com.migros_one.courier_tracking.courier.mapper.CourierLastStoreEntranceMapper;
import com.migros_one.courier_tracking.courier.repository.CourierLastStoreEntranceRepository;
import com.migros_one.courier_tracking.courier.repository.CourierRepository;
import com.migros_one.courier_tracking.courier.service.intf.CourierService;
import com.migros_one.courier_tracking.store.dto.StoreDTO;
import com.migros_one.courier_tracking.store.entity.StoreEntity;
import com.migros_one.courier_tracking.store.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class CourierServiceImpl implements CourierService {

    private static final Logger log = LoggerFactory.getLogger(CourierServiceImpl.class);
    private final CourierLastStoreEntranceRepository lastStoreEntranceRepository;
    private final CourierLastStoreEntranceMapper lastStoreEntranceMapper;
    private final CourierRepository courierRepository;
    private final StoreRepository storeRepository;

    @Override
    public void checkCourierNearToStoreForUpdate(CourierLocationDTO courierLocationDTO, List<StoreDTO> storeDTOList){
        for (StoreDTO storeDTO : storeDTOList) {
            double distance = calculateDistance(courierLocationDTO.getLatitude(), courierLocationDTO.getLongitude(), storeDTO.getLatitude(), storeDTO.getLongitude());
            if (distance <= 100) {
                checkLastEntranceAndUpdate(courierLocationDTO, storeDTO.getName(), distance);
            }
        }
    }

    protected double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int EARTH_RADIUS = 6371; // Kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    protected void checkLastEntranceAndUpdate(CourierLocationDTO courierLocationDTO, String storeName, double distance) {
        if(isEntranceUpdateNeeded(courierLocationDTO.getCourierId(),storeName)) {
            Optional<CourierLastStoreEntranceEntity> possibleLastEntrance = getCourierLastStoreEntranceEntityByCourierIdAndStoreName(courierLocationDTO.getCourierId(), storeName);
            if(possibleLastEntrance.isPresent()){
                CourierLastStoreEntranceEntity courierLastStoreEntranceEntity = possibleLastEntrance.get();
                replaceLastEntrance(courierLastStoreEntranceEntity, courierLocationDTO.getTime());
            } else { //first entrance
                try{
                    saveNewEntrance(courierLocationDTO.getCourierId(),storeName, courierLocationDTO.getTime());
                } catch (Exception e){ //TODO: change here
                    log.error("cannot save entrance", e.getMessage());
                }
            }
            log.info("Courier " + courierLocationDTO.getCourierId() + " close to " + storeName + " with the distance "+ distance);
        }
    }

    protected boolean isEntranceUpdateNeeded(Long courierId, String storeName){
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(1);
        return lastStoreEntranceRepository.findRecentEntrance(courierId, storeName, thresholdTime).isEmpty();
    }

    protected Optional<CourierLastStoreEntranceEntity> getCourierLastStoreEntranceEntityByCourierIdAndStoreName(Long courierId, String storeName) {
        return lastStoreEntranceRepository.findByCourier_IdAndStore_Name(courierId, storeName);
    }

    protected void replaceLastEntrance(CourierLastStoreEntranceEntity presentStoreEntrance, LocalDateTime newEntranceTime){
        presentStoreEntrance.setLastEntrance(newEntranceTime);
        lastStoreEntranceMapper.entityToDTO(lastStoreEntranceRepository.save(presentStoreEntrance));
    }

    protected void saveNewEntrance(Long courierId, String storeName, LocalDateTime entranceTime) throws Exception {
        Optional<StoreEntity> possibleStoreEntity = storeRepository.findByName(storeName);
        Optional<CourierEntity> possibleCourierEntity = courierRepository.findById(courierId);

        if(possibleStoreEntity.isEmpty() || possibleCourierEntity.isEmpty()){
            throw new Exception("data not found"); //TODO: EXCEPTION CLASSES
        }

        CourierLastStoreEntranceDTO newEntrance = new CourierLastStoreEntranceDTO();
        newEntrance.setCourier(possibleCourierEntity.get());
        newEntrance.setStore(possibleStoreEntity.get());
        newEntrance.setLastEntrance(entranceTime);
        lastStoreEntranceRepository.save(lastStoreEntranceMapper.dtoToEntity(newEntrance));
    }


}
