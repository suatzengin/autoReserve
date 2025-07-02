package com.autoreserve.service;

import com.autoreserve.dto.CarTypeResponse;
import com.autoreserve.model.CarType;
import com.autoreserve.repository.CarTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarTypeService {

    private final CarTypeRepository carTypeRepository;

    public CarTypeService(CarTypeRepository carTypeRepository) {
        this.carTypeRepository = carTypeRepository;
    }

    public List<CarTypeResponse> getAllCarTypeResponses() {
        return carTypeRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<CarTypeResponse> getCarTypeById(String id) {
        return carTypeRepository.findById(id)
                .map(this::mapToResponse);
    }

    private CarTypeResponse mapToResponse(CarType carType) {
        return new CarTypeResponse(carType.getId(), carType.getName(), carType.getTotalStock());
    }

}