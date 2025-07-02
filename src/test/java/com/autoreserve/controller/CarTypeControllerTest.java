package com.autoreserve.controller;

import com.autoreserve.dto.CarTypeResponse;
import com.autoreserve.service.CarTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarTypeControllerTest {

    @Mock
    private CarTypeService carTypeService;

    @InjectMocks
    private CarTypeController carTypeController;

    @Test
    void shouldReturnAllCarTypes() {
        List<CarTypeResponse> mockList = List.of(
                new CarTypeResponse("1", "Sedan", 5),
                new CarTypeResponse("2", "SUV", 3)
        );

        when(carTypeService.getAllCarTypeResponses()).thenReturn(mockList);

        List<CarTypeResponse> result = carTypeController.getAllCarTypes();

        assertEquals(2, result.size());
        assertEquals("Sedan", result.getFirst().name());
        verify(carTypeService).getAllCarTypeResponses();
    }

    @Test
    void shouldReturnCarTypeByIdIfFound() {
        CarTypeResponse response = new CarTypeResponse("1", "Sedan", 5);
        when(carTypeService.getCarTypeById("1")).thenReturn(Optional.of(response));

        ResponseEntity<CarTypeResponse> result = carTypeController.getCarTypeById("1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Sedan", result.getBody().name());
        verify(carTypeService).getCarTypeById("1");
    }

    @Test
    void shouldReturnNotFoundIfCarTypeMissing() {
        when(carTypeService.getCarTypeById("99")).thenReturn(Optional.empty());

        ResponseEntity<CarTypeResponse> result = carTypeController.getCarTypeById("99");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
        verify(carTypeService).getCarTypeById("99");
    }
}
