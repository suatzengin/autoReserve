package com.autoreserve.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Data
@NoArgsConstructor
public class Reservation {

    @Id
    private String id;

    private String customerName;

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private String carTypeId;

    public int getDurationDays() {
        return (int) ChronoUnit.DAYS.between(startDatetime, endDatetime);
    }
}
