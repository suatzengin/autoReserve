package com.autoreserve.service;

import com.autoreserve.model.CarType;
import com.autoreserve.model.Reservation;
import com.autoreserve.repository.CarTypeRepository;
import com.autoreserve.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilityService {

    private final ReservationRepository reservationRepository;
    private final CarTypeRepository carTypeRepository;

    public AvailabilityService(ReservationRepository reservationRepository, CarTypeRepository carTypeRepository) {
        this.reservationRepository = reservationRepository;
        this.carTypeRepository = carTypeRepository;
    }

    public int getAvailableCount(String carTypeId, LocalDateTime start, LocalDateTime end) {
        int totalStock = carTypeRepository.findById(carTypeId)
                .map(CarType::getTotalStock)
                .orElse(0);

        if (totalStock == 0) return 0;

        List<Reservation> overlapping = reservationRepository.findByCarTypeIdAndStatusAndDateRange(
                carTypeId, start, end);

        return totalStock - overlapping.size();
    }

    public boolean carTypeExists(String carTypeId) {
        return carTypeRepository.existsById(carTypeId);
    }
}
