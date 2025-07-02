package com.autoreserve.repository;

import com.autoreserve.model.Reservation;
import com.autoreserve.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Query("SELECT r FROM Reservation r WHERE r.carTypeId = :carTypeId AND r.status = 'RESERVED' " +
            "AND ((r.startDatetime < :end) AND (r.endDatetime > :start))")
    List<Reservation> findByCarTypeIdAndStatusAndDateRange(String carTypeId,
                                                           LocalDateTime start,
                                                           LocalDateTime end);
}
