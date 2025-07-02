package com.autoreserve.service;

import com.autoreserve.dto.CarTypeResponse;
import com.autoreserve.model.CarType;
import com.autoreserve.repository.CarTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CarTypeServiceTest {

    @Mock
    private CarTypeRepository carTypeRepository;

    @InjectMocks
    private CarTypeService carTypeService;

    private CarType sedan;

    @BeforeEach
    void setUp() {
        sedan = new CarType();
        sedan.setId("1");
        sedan.setName("Sedan");
        sedan.setTotalStock(10);
    }

    @Test
    void shouldReturnAllCarTypes() {
        when(carTypeRepository.findAll()).thenReturn(List.of(sedan));

        List<CarTypeResponse> result = carTypeService.getAllCarTypeResponses();

        assertEquals(1, result.size());
        assertEquals("Sedan", result.getFirst().name());
        verify(carTypeRepository).findAll();
    }

    @Test
    void shouldReturnCarTypeByIdIfExists() {
        when(carTypeRepository.findById("1")).thenReturn(Optional.of(sedan));

        Optional<CarTypeResponse> result = carTypeService.getCarTypeById("1");

        assertTrue(result.isPresent());
        assertEquals("Sedan", result.get().name());
        verify(carTypeRepository).findById("1");
    }

    @Test
    void shouldReturnEmptyIfCarTypeNotFound() {
        when(carTypeRepository.findById("99")).thenReturn(Optional.empty());

        Optional<CarTypeResponse> result = carTypeService.getCarTypeById("99");

        assertTrue(result.isEmpty());
        verify(carTypeRepository).findById("99");
    }
}
