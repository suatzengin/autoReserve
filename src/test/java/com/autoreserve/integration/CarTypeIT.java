package com.autoreserve.integration;

import com.autoreserve.model.CarType;
import com.autoreserve.repository.CarTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarTypeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarTypeRepository carTypeRepository;

    @BeforeEach
    void setUp() {
        carTypeRepository.deleteAll();

        CarType sedan = new CarType();
        sedan.setId("1");
        sedan.setName("Sedan");
        sedan.setTotalStock(10);

        carTypeRepository.save(sedan);
    }

    @Test
    void shouldReturnAllCarTypes() throws Exception {
        mockMvc.perform(get("/api/car-types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Sedan"))
                .andExpect(jsonPath("$[0].totalStock").value(10));
    }

    @Test
    void shouldReturnCarTypeById() throws Exception {
        mockMvc.perform(get("/api/car-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Sedan"));
    }
}
