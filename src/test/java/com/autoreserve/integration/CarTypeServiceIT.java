package com.autoreserve.integration;

import com.autoreserve.dto.CarTypeResponse;
import com.autoreserve.model.CarType;
import com.autoreserve.service.CarTypeService;
import com.autoreserve.repository.CarTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarTypeServiceIT {

    @Autowired
    private CarTypeService carTypeService;

    @Autowired
    private CarTypeRepository carTypeRepository;

    @BeforeEach
    void setup() {
        carTypeRepository.deleteAll();

        CarType sedan = new CarType();
        sedan.setId("1");
        sedan.setName("Sedan");
        sedan.setTotalStock(5);

        CarType suv = new CarType();
        suv.setId("2");
        suv.setName("SUV");
        suv.setTotalStock(3);

        carTypeRepository.saveAll(List.of(sedan, suv));
    }

    @Test
    void shouldReturnAllCarTypes() {
        List<CarTypeResponse> result = carTypeService.getAllCarTypeResponses();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(ct -> ct.name().equals("Sedan")));
        assertTrue(result.stream().anyMatch(ct -> ct.name().equals("SUV")));
    }

    @Test
    void shouldReturnCarTypeById() {
        Optional<CarTypeResponse> result = carTypeService.getCarTypeById("1");

        assertTrue(result.isPresent());
        assertEquals("Sedan", result.get().name());
        assertEquals(5, result.get().totalStock());
    }

    @Test
    void shouldReturnEmptyWhenCarTypeNotFound() {
        Optional<CarTypeResponse> result = carTypeService.getCarTypeById("999");
        assertTrue(result.isEmpty());
    }
}
