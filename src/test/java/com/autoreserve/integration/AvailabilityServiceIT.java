package com.autoreserve.integration;

import com.autoreserve.model.CarType;
import com.autoreserve.model.Reservation;
import com.autoreserve.model.ReservationStatus;
import com.autoreserve.repository.CarTypeRepository;
import com.autoreserve.repository.ReservationRepository;
import com.autoreserve.service.AvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AvailabilityServiceIT {

    @Autowired
    private AvailabilityService availabilityService;

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
        sedan.setTotalStock(3);
        carTypeRepository.save(sedan);

        Reservation r1 = new Reservation();
        r1.setId("res1");
        r1.setCustomerName("John");
        r1.setCarTypeId("1");
        r1.setStartDatetime(LocalDateTime.of(2025, 7, 1, 10, 0));
        r1.setEndDatetime(LocalDateTime.of(2025, 7, 3, 10, 0));
        r1.setStatus(ReservationStatus.RESERVED);

        reservationRepository.save(r1);
    }

    @Test
    void shouldReturnCorrectAvailability() {
        LocalDateTime start = LocalDateTime.of(2025, 7, 2, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 7, 4, 0, 0);

        int available = availabilityService.getAvailableCount("1", start, end);
        assertEquals(2, available); // 3 total - 1 reserved
    }

    @Test
    void shouldReturnZeroIfCarTypeNotFound() {
        LocalDateTime start = LocalDateTime.of(2025, 7, 2, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 7, 4, 0, 0);

        int available = availabilityService.getAvailableCount("999", start, end);
        assertEquals(0, available);
    }

    @Test
    void shouldReturnTrueIfCarTypeExists() {
        assertTrue(availabilityService.carTypeExists("1"));
    }

    @Test
    void shouldReturnFalseIfCarTypeMissing() {
        assertFalse(availabilityService.carTypeExists("999"));
    }
}
