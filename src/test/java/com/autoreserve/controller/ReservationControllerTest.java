package com.autoreserve.controller;

import com.autoreserve.dto.CreateReservationRequest;
import com.autoreserve.dto.ReservationResponse;
import com.autoreserve.exception.BadRequestException;
import com.autoreserve.model.ReservationStatus;
import com.autoreserve.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    void shouldCreateReservation() {
        CreateReservationRequest request = new CreateReservationRequest(
                "Alice",
                LocalDateTime.of(2025, 7, 1, 10, 0),
                LocalDateTime.of(2025, 7, 3, 10, 0),
                "1"
        );

        ReservationResponse response = new ReservationResponse(
                "abc123",
                "Alice",
                request.getStartDatetime(),
                request.getEndDatetime(),
                (int) ChronoUnit.DAYS.between(request.getStartDatetime(), request.getEndDatetime()),
                ReservationStatus.RESERVED,
                "Sedan"
        );

        when(reservationService.createReservation(request)).thenReturn(response);

        ReservationResponse result = reservationController.createReservation(request);
        assertEquals("Alice", result.customerName());
        verify(reservationService).createReservation(request);
    }

    @Test
    void shouldThrowBadRequestForInvalidDateRange() {
        CreateReservationRequest invalid = new CreateReservationRequest(
                "Bob",
                LocalDateTime.of(2025, 7, 3, 10, 0),
                LocalDateTime.of(2025, 7, 3, 10, 0),
                "2"
        );

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                reservationController.createReservation(invalid)
        );

        assertEquals("Start time must be before end time.", ex.getMessage());
    }

    @Test
    void shouldReturnAllReservations() {
        when(reservationService.getAllReservations()).thenReturn(List.of());
        assertEquals(0, reservationController.getAllReservations().size());
        verify(reservationService).getAllReservations();
    }

    @Test
    void shouldGetReservationById() {
        String id = "abc";
        ReservationResponse response = new ReservationResponse(
                id, "Jane", LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1,
                ReservationStatus.RESERVED, "SUV"
        );

        when(reservationService.getReservationById(id)).thenReturn(Optional.of(response));

        Optional<ReservationResponse> result = reservationController.getReservationById(id);
        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().customerName());
    }

    @Test
    void shouldCancelReservation() {
        reservationController.cancelReservation("xyz");
        verify(reservationService).cancelReservation("xyz");
    }
}
