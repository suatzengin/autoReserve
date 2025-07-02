package com.autoreserve.controller;

import com.autoreserve.dto.CarTypeResponse;
import com.autoreserve.service.CarTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/car-types")
public class CarTypeController {

    private final CarTypeService carTypeService;

    public CarTypeController(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
    }

    @GetMapping
    public List<CarTypeResponse> getAllCarTypes() {
        return carTypeService.getAllCarTypeResponses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarTypeResponse> getCarTypeById(@PathVariable String id) {
        return carTypeService.getCarTypeById(id)
                .map(carType -> new CarTypeResponse(
                        carType.id(),
                        carType.name(),
                        carType.totalStock()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
