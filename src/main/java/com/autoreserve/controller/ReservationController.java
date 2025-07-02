package com.autoreserve.controller;

import com.autoreserve.dto.CreateReservationRequest;
import com.autoreserve.dto.ReservationResponse;
import com.autoreserve.exception.BadRequestException;
import com.autoreserve.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationResponse createReservation(@Valid @RequestBody CreateReservationRequest request) {
        if (request.getStartDatetime().isAfter(request.getEndDatetime()) ||
                request.getStartDatetime().equals(request.getEndDatetime())) {
            throw new BadRequestException("Start time must be before end time.");
        }

        return reservationService.createReservation(request);
    }

    @GetMapping
    public List<ReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Optional<ReservationResponse> getReservationById(@PathVariable String id) {
        return reservationService.getReservationById(id);
    }

    @DeleteMapping("/{id}")
    public void cancelReservation(@PathVariable String id) {
        reservationService.cancelReservation(id);
    }
}
