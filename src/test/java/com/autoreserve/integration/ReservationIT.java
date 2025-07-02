package com.autoreserve.integration;

import com.autoreserve.model.CarType;
import com.autoreserve.repository.CarTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.autoreserve.dto.CreateReservationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        carTypeRepository.deleteAll();
        CarType carType = new CarType();
        carType.setId("1");
        carType.setName("Sedan");
        carType.setTotalStock(3);
        carTypeRepository.save(carType);
    }

    @Test
    void shouldCreateReservationSuccessfully() throws Exception {
        CreateReservationRequest request = new CreateReservationRequest(
                "Alice",
                LocalDateTime.of(2025, 7, 1, 10, 0),
                LocalDateTime.of(2025, 7, 3, 10, 0),
                "1"
        );

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.carTypeName").value("Sedan"));
    }

    @Test
    void shouldRejectReservationWithInvalidDates() throws Exception {
        CreateReservationRequest request = new CreateReservationRequest(
                "Bob",
                LocalDateTime.of(2025, 7, 3, 10, 0),
                LocalDateTime.of(2025, 7, 3, 10, 0),
                "1"
        );

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Start time must be before end time."));
    }
}
