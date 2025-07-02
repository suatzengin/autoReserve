package com.autoreserve.service;

import com.autoreserve.dto.CreateReservationRequest;
import com.autoreserve.dto.ReservationResponse;
import com.autoreserve.model.CarType;
import com.autoreserve.model.Reservation;
import com.autoreserve.model.ReservationStatus;
import com.autoreserve.repository.CarTypeRepository;
import com.autoreserve.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CarTypeRepository carTypeRepository;

    @InjectMocks
    private ReservationService reservationService;

    private CreateReservationRequest request;
    private Reservation reservation;
    private CarType carType;

    @BeforeEach
    void setup() {
        request = new CreateReservationRequest(
                "Alice",
                LocalDateTime.of(2025, 7, 1, 10, 0),
                LocalDateTime.of(2025, 7, 3, 10, 0),
                "1"
        );

        reservation = new Reservation();
        reservation.setId("res123");
        reservation.setCustomerName(request.getCustomerName());
        reservation.setStartDatetime(request.getStartDatetime());
        reservation.setEndDatetime(request.getEndDatetime());
        reservation.setCarTypeId(request.getCarTypeId());
        reservation.setStatus(ReservationStatus.RESERVED);

        carType = new CarType();
        carType.setId("1");
        carType.setName("Sedan");
        carType.setTotalStock(5);
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        // mock save behavior
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(carTypeRepository.findById("1")).thenReturn(Optional.of(carType));

        ReservationResponse response = reservationService.createReservation(request);

        assertNotNull(response.id());
        assertEquals("Alice", response.customerName());
        assertEquals("Sedan", response.carTypeName());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldGetAllReservations() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        when(carTypeRepository.findById("1")).thenReturn(Optional.of(carType));

        List<ReservationResponse> result = reservationService.getAllReservations();

        assertEquals(1, result.size());
        assertEquals("Sedan", result.getFirst().carTypeName());
    }

    @Test
    void shouldReturnReservationById() {
        when(reservationRepository.findById("res123")).thenReturn(Optional.of(reservation));
        when(carTypeRepository.findById("1")).thenReturn(Optional.of(carType));

        Optional<ReservationResponse> result = reservationService.getReservationById("res123");

        assertTrue(result.isPresent());
        assertEquals("Sedan", result.get().carTypeName());
    }

    @Test
    void shouldCancelReservation() {
        reservation.setStatus(ReservationStatus.RESERVED);
        when(reservationRepository.findById("res123")).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation("res123");

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        verify(reservationRepository).save(reservation);
    }
}
