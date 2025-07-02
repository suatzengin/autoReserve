package com.autoreserve.dto;

import com.autoreserve.model.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse (
        String id,
        String customerName,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime,
        int durationDays,
        ReservationStatus status,
        String carTypeName
) {}
