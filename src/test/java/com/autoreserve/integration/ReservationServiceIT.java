package com.autoreserve.integration;

import com.autoreserve.dto.CreateReservationRequest;
import com.autoreserve.dto.ReservationResponse;
import com.autoreserve.model.CarType;
import com.autoreserve.model.ReservationStatus;
import com.autoreserve.repository.CarTypeRepository;
import com.autoreserve.repository.ReservationRepository;
import com.autoreserve.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceIT {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        reservationRepository.deleteAll();
        carTypeRepository.deleteAll();

        CarType sedan = new CarType();
        sedan.setId("1");
        sedan.setName("Sedan");
        sedan.setTotalStock(5);
        carTypeRepository.save(sedan);
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        CreateReservationRequest request = new CreateReservationRequest(
                "Alice",
                LocalDateTime.of(2025, 7, 1, 10, 0),
                LocalDateTime.of(2025, 7, 3, 10, 0),
                "1"
        );

        ReservationResponse response = reservationService.createReservation(request);

        assertNotNull(response.id());
        assertEquals("Alice", response.customerName());
        assertEquals("Sedan", response.carTypeName());

        assertEquals(1, reservationRepository.count());
    }

    @Test
    void shouldGetAllReservations() {
        // Create a reservation first
        shouldCreateReservationSuccessfully();

        List<ReservationResponse> all = reservationService.getAllReservations();
        assertEquals(1, all.size());
        assertEquals("Sedan", all.getFirst().carTypeName());
    }

    @Test
    void shouldGetReservationById() {
        CreateReservationRequest request = new CreateReservationRequest(
                "Bob",
                LocalDateTime.of(2025, 8, 1, 10, 0),
                LocalDateTime.of(2025, 8, 5, 10, 0),
                "1"
        );

        ReservationResponse saved = reservationService.createReservation(request);
        Optional<ReservationResponse> found = reservationService.getReservationById(saved.id());

        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().customerName());
    }

    @Test
    void shouldCancelReservation() {
        CreateReservationRequest request = new CreateReservationRequest(
                "Charlie",
                LocalDateTime.of(2025, 9, 1, 10, 0),
                LocalDateTime.of(2025, 9, 2, 10, 0),
                "1"
        );

        ReservationResponse saved = reservationService.createReservation(request);
        reservationService.cancelReservation(saved.id());

        assertEquals(ReservationStatus.CANCELLED,
                reservationRepository.findById(saved.id()).get().getStatus());
    }
}
