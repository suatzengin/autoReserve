package com.autoreserve.integration;

import com.autoreserve.model.CarType;
import com.autoreserve.model.Reservation;
import com.autoreserve.model.ReservationStatus;
import com.autoreserve.repository.CarTypeRepository;
import com.autoreserve.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AvailabilityIT {

    @Autowired
    private MockMvc mockMvc;

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
        r1.setStartDatetime(LocalDateTime.of(2025, 7, 1, 10, 0));
        r1.setEndDatetime(LocalDateTime.of(2025, 7, 2, 10, 0));
        r1.setStatus(ReservationStatus.RESERVED);
        r1.setCarTypeId("1");
        reservationRepository.save(r1);
    }

    @Test
    void shouldReturnCorrectAvailability() throws Exception {
        mockMvc.perform(get("/api/availability")
                        .param("carTypeId", "1")
                        .param("start", "2025-07-01T10:00:00")
                        .param("end", "2025-07-02T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(2));
    }

    @Test
    void shouldReturn400ForMissingCarType() throws Exception {
        mockMvc.perform(get("/api/availability")
                        .param("carTypeId", "999")
                        .param("start", "2025-07-01T10:00:00")
                        .param("end", "2025-07-02T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Car type not found: 999"));
    }
}
