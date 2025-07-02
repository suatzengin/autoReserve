package com.autoreserve.controller;

import com.autoreserve.dto.AvailabilityResponse;
import com.autoreserve.exception.BadRequestException;
import com.autoreserve.service.AvailabilityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping
    public AvailabilityResponse getAvailability(
            @RequestParam String carTypeId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {
        if (start.isAfter(end) || start.equals(end)) {
            throw new BadRequestException("Start time must be before end time.");
        }

        if (!availabilityService.carTypeExists(carTypeId)) {
            throw new BadRequestException("Car type not found: " + carTypeId);
        }

        int available = availabilityService.getAvailableCount(carTypeId, start, end);
        return new AvailabilityResponse(available);
    }
}
