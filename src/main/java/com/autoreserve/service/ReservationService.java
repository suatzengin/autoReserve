package com.autoreserve.service;

import com.autoreserve.dto.CreateReservationRequest;
import com.autoreserve.dto.ReservationResponse;
import com.autoreserve.model.CarType;
import com.autoreserve.model.Reservation;
import com.autoreserve.model.ReservationStatus;
import com.autoreserve.repository.CarTypeRepository;
import com.autoreserve.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CarTypeRepository carTypeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              CarTypeRepository carTypeRepository) {
        this.reservationRepository = reservationRepository;
        this.carTypeRepository = carTypeRepository;
    }

    public ReservationResponse createReservation(CreateReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setId(UUID.randomUUID().toString());
        reservation.setCustomerName(request.getCustomerName());
        reservation.setStartDatetime(request.getStartDatetime());
        reservation.setEndDatetime(request.getEndDatetime());
        reservation.setCarTypeId(request.getCarTypeId());
        reservation.setStatus(ReservationStatus.RESERVED);
        reservationRepository.save(reservation);

        return mapToResponse(reservation);
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<ReservationResponse> getReservationById(String id) {
        return reservationRepository.findById(id).map(this::mapToResponse);
    }

    public void cancelReservation(String id) {
        reservationRepository.findById(id).ifPresent(reservation -> {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        });
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        String carTypeName = carTypeRepository.findById(reservation.getCarTypeId())
                .map(CarType::getName)
                .orElse("Unknown");

        return new ReservationResponse(
                reservation.getId(),
                reservation.getCustomerName(),
                reservation.getStartDatetime(),
                reservation.getEndDatetime(),
                reservation.getDurationDays(),
                reservation.getStatus(),
                carTypeName
        );
    }
}
