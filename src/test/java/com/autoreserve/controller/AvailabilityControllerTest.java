package com.autoreserve.controller;

import com.autoreserve.dto.AvailabilityResponse;
import com.autoreserve.exception.BadRequestException;
import com.autoreserve.service.AvailabilityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvailabilityControllerTest {

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private AvailabilityController availabilityController;

    @Test
    void shouldReturnAvailabilityWhenInputsAreValid() {
        String carTypeId = "1";
        LocalDateTime start = LocalDateTime.of(2025, 7, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 7, 3, 10, 0);

        when(availabilityService.carTypeExists(carTypeId)).thenReturn(true);
        when(availabilityService.getAvailableCount(carTypeId, start, end)).thenReturn(2);

        AvailabilityResponse response = availabilityController.getAvailability(carTypeId, start, end);

        assertEquals(2, response.available());
        verify(availabilityService).carTypeExists(carTypeId);
        verify(availabilityService).getAvailableCount(carTypeId, start, end);
    }

    @Test
    void shouldThrowBadRequestWhenStartAfterEnd() {
        LocalDateTime start = LocalDateTime.of(2025, 7, 3, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 7, 1, 10, 0);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> availabilityController.getAvailability("1", start, end)
        );

        assertEquals("Start time must be before end time.", ex.getMessage());
    }

    @Test
    void shouldThrowBadRequestWhenCarTypeNotFound() {
        String carTypeId = "99";
        LocalDateTime start = LocalDateTime.of(2025, 7, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 7, 3, 10, 0);

        when(availabilityService.carTypeExists(carTypeId)).thenReturn(false);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> availabilityController.getAvailability(carTypeId, start, end)
        );

        assertEquals("Car type not found: 99", ex.getMessage());
        verify(availabilityService).carTypeExists(carTypeId);
    }
}
