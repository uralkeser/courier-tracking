package com.migros_one.courier_tracking.courier.service.impl;

import com.migros_one.courier_tracking.courier.dto.CourierDTO;
import com.migros_one.courier_tracking.courier.entity.CourierEntity;
import com.migros_one.courier_tracking.courier.entity.CourierStoreEntranceLogEntity;
import com.migros_one.courier_tracking.courier.entity.CourierTotalDistanceEntity;
import com.migros_one.courier_tracking.courier.mapper.CourierMapper;
import com.migros_one.courier_tracking.courier.repository.CourierStoreEntranceLogRepository;
import com.migros_one.courier_tracking.courier.repository.CourierRepository;
import com.migros_one.courier_tracking.courier.repository.CourierTotalDistanceRepository;
import com.migros_one.courier_tracking.courier.service.intf.CourierService;
import com.migros_one.courier_tracking.store.dto.StoreDTO;
import com.migros_one.courier_tracking.store.entity.StoreEntity;
import com.migros_one.courier_tracking.store.service.intf.StoreService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CourierServiceImpl implements CourierService {

    private static final Logger log = LoggerFactory.getLogger(CourierServiceImpl.class);
    private final CourierStoreEntranceLogRepository courierStoreEntranceLogRepository;
    private final CourierRepository courierRepository;
    private final CourierMapper courierMapper;
    private final StoreService storeService;
    private final CourierTotalDistanceRepository courierTotalDistanceRepository;

    @Override
    @Transactional
    public void logCourierNearToStore(CourierDTO newCourierDTO) throws Exception{
        List<StoreDTO> storeDTOList = storeService.loadStores();
        Optional<CourierEntity> previousCourierEntity = courierRepository.findById(newCourierDTO.getId());
        if(previousCourierEntity.isPresent()) {
            saveCourierEntranceForAllStores(newCourierDTO, storeDTOList);
        } else { //new courier
            courierRepository.saveAndFlush(courierMapper.dtoToEntity(newCourierDTO));
            saveCourierEntranceForAllStores(newCourierDTO, storeDTOList);
        }
    }

    protected void saveCourierEntranceForAllStores(CourierDTO courierDTO, List<StoreDTO> storeDTOList){
        for (StoreDTO storeDTO : storeDTOList) {
            double distance = calculateDistance(courierDTO.getLatitude(), courierDTO.getLongitude(), storeDTO.getLatitude(), storeDTO.getLongitude());
            if (distance <= 100 && isOneMinuteElapsed(courierDTO, storeDTO.getName())) {
                try {
                    saveNewEntrance(courierDTO.getId(), storeDTO.getName(), courierDTO.getTime());
                } catch (Exception e) {
                    log.error("can not save for the reason:"+e.getMessage());
                }
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
        return EARTH_RADIUS * c * 1000; // km to m
    }

    protected boolean isOneMinuteElapsed(CourierDTO courierDTO, String storeName){
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        return courierStoreEntranceLogRepository.findRecentEntrance(courierDTO.getId(), storeName, oneMinuteAgo).isEmpty();
    }

    protected void saveNewEntrance(Long courierId, String storeName, LocalDateTime entranceTime) throws Exception {
        Optional<StoreEntity> possibleStoreEntity = storeService.getByName(storeName);
        Optional<CourierEntity> possibleCourierEntity = courierRepository.findById(courierId);

        if(possibleStoreEntity.isEmpty() || possibleCourierEntity.isEmpty()){
            throw new Exception("store or courier not found"); //TODO: EXCEPTION CLASSES
        }

        CourierStoreEntranceLogEntity newEntrance = new CourierStoreEntranceLogEntity();
        newEntrance.setCourier(possibleCourierEntity.get());
        newEntrance.setStore(possibleStoreEntity.get());
        newEntrance.setEntrance(entranceTime);
        courierStoreEntranceLogRepository.save(newEntrance);
    }

    @Override
    public void updateTotalDistance(CourierDTO newCourierDTO) throws Exception {
        Optional<CourierEntity> previousCourierEntity = courierRepository.findById(newCourierDTO.getId());
        if(previousCourierEntity.isEmpty()) {
            throw new Exception("Courier not found"); //TODO: EXCEPTION CLASSES
        }
        CourierDTO previousCourierDTO = courierMapper.entityToDTO(previousCourierEntity.get());
        double newDistance = calculateNewDistance(newCourierDTO, previousCourierDTO);
        Optional<CourierTotalDistanceEntity> possibleCourierTotalDistanceEntity = courierTotalDistanceRepository.findByCourier_Id(previousCourierDTO.getId());
        if(possibleCourierTotalDistanceEntity.isPresent()){
            CourierTotalDistanceEntity entity = possibleCourierTotalDistanceEntity.get();
            Double newTotalDistance = entity.getTotalDistance() + newDistance;
            entity.setTotalDistance(newTotalDistance);
            courierTotalDistanceRepository.save(entity);
        } else { //first time
            //TODO mapper
            CourierTotalDistanceEntity courierTotalDistanceEntity =  new CourierTotalDistanceEntity();
            courierTotalDistanceEntity.setCourier(courierMapper.dtoToEntity(previousCourierDTO));
            courierTotalDistanceEntity.setTotalDistance(newDistance);
            courierTotalDistanceRepository.save(courierTotalDistanceEntity);
        }
    }

    protected double calculateNewDistance(CourierDTO newCourierDTO, CourierDTO previousCourierDTO){
        return calculateDistance(newCourierDTO.getLatitude(), newCourierDTO.getLongitude(),
                previousCourierDTO.getLatitude(), previousCourierDTO.getLongitude());
    }

    @Override
    @Transactional
    public void updateCourier(CourierDTO newCourierDTO){
        courierRepository.save(courierMapper.dtoToEntity(newCourierDTO));
    }

    @Override
    public Double getTotalTravelDistance(Long courierId) throws Exception  {
        Optional<CourierTotalDistanceEntity> possibleCourierTotalDistanceEntity = courierTotalDistanceRepository.findByCourier_Id(courierId);
        if(possibleCourierTotalDistanceEntity.isPresent()){
            return possibleCourierTotalDistanceEntity.get().getTotalDistance();
        } else {
            throw new Exception("Courier not found"); //TODO
        }
    }





}
