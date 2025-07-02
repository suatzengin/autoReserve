package com.autoreserve.service;

import com.autoreserve.model.CarType;
import com.autoreserve.model.Reservation;
import com.autoreserve.model.ReservationStatus;
import com.autoreserve.repository.CarTypeRepository;
import com.autoreserve.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private CarTypeRepository carTypeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    private CarType sedan;

    @BeforeEach
    void setUp() {
        sedan = new CarType();
        sedan.setId("1");
        sedan.setName("Sedan");
        sedan.setTotalStock(3);
    }

    @Test
    void shouldReturnZeroWhenCarTypeNotFound() {
        when(carTypeRepository.findById("999")).thenReturn(Optional.empty());

        int available = availabilityService.getAvailableCount("999",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        assertEquals(0, available);
    }

    @Test
    void shouldReturnCorrectAvailableCount() {
        when(carTypeRepository.findById("1")).thenReturn(Optional.of(sedan));

        LocalDateTime start = LocalDateTime.of(2025, 7, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 7, 3, 10, 0);

        Reservation r1 = new Reservation();
        r1.setId("r1");
        r1.setStatus(ReservationStatus.RESERVED);
        r1.setStartDatetime(start);
        r1.setEndDatetime(end);
        r1.setCarTypeId("1");

        when(reservationRepository.findByCarTypeIdAndStatusAndDateRange("1", start, end))
                .thenReturn(List.of(r1));

        int available = availabilityService.getAvailableCount("1", start, end);

        assertEquals(2, available);
    }

    @Test
    void shouldReturnTrueIfCarTypeExists() {
        when(carTypeRepository.existsById("1")).thenReturn(true);
        assertTrue(availabilityService.carTypeExists("1"));
    }

    @Test
    void shouldReturnFalseIfCarTypeDoesNotExist() {
        when(carTypeRepository.existsById("999")).thenReturn(false);
        assertFalse(availabilityService.carTypeExists("999"));
    }

    @Test
    void shouldReturnCorrectCountWithMultipleOverlappingReservations() {
        when(carTypeRepository.findById("1")).thenReturn(Optional.of(sedan));

        LocalDateTime queryStart = LocalDateTime.of(2025, 7, 1, 10, 0);
        LocalDateTime queryEnd   = LocalDateTime.of(2025, 7, 3, 10, 0);

        Reservation r1 = new Reservation(); // overlaps at start
        r1.setCarTypeId("1");
        r1.setStatus(ReservationStatus.RESERVED);
        r1.setStartDatetime(LocalDateTime.of(2025, 6, 30, 10, 0));
        r1.setEndDatetime(LocalDateTime.of(2025, 7, 1, 12, 0));

        Reservation r2 = new Reservation(); // fully inside
        r2.setCarTypeId("1");
        r2.setStatus(ReservationStatus.RESERVED);
        r2.setStartDatetime(LocalDateTime.of(2025, 7, 2, 0, 0));
        r2.setEndDatetime(LocalDateTime.of(2025, 7, 2, 23, 59));

        Reservation r3 = new Reservation(); // overlaps at end
        r3.setCarTypeId("1");
        r3.setStatus(ReservationStatus.RESERVED);
        r3.setStartDatetime(LocalDateTime.of(2025, 7, 2, 23, 59));
        r3.setEndDatetime(LocalDateTime.of(2025, 7, 4, 10, 0));

        when(reservationRepository.findByCarTypeIdAndStatusAndDateRange("1", queryStart, queryEnd))
                .thenReturn(List.of(r1, r2, r3));

        int available = availabilityService.getAvailableCount("1", queryStart, queryEnd);

        assertEquals(0, available); // 3 total - 3 overlapping
    }

    @Test
    void shouldReturnFullStockWhenNoReservationsOverlap() {
        when(carTypeRepository.findById("1")).thenReturn(Optional.of(sedan));

        LocalDateTime queryStart = LocalDateTime.of(2025, 7, 10, 10, 0);
        LocalDateTime queryEnd   = LocalDateTime.of(2025, 7, 12, 10, 0);

        when(reservationRepository.findByCarTypeIdAndStatusAndDateRange("1", queryStart, queryEnd))
                .thenReturn(Collections.emptyList());

        int available = availabilityService.getAvailableCount("1", queryStart, queryEnd);

        assertEquals(3, available); // no overlaps
    }
}
