package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUser_InvalidSalary_ShouldFail() throws Exception {
        String invalidUser = "[{\"id\":6,\"name\":\"Carlos\",\"salary\":-1000}]";
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_MissingRequiredFields_ShouldFail() throws Exception {
        String invalidUser = "[{\"id\":7,\"cedula\":\"123456\"}]"; // Falta nombre
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUser))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_InvalidCedulaFormat_ShouldFail() throws Exception {
        String validUser = "[{\"id\":8,\"name\":\"Marta\",\"cedula\":\"ABC123\",\"salary\":2000}]";
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validUser));

        String invalidUpdate = "{\"cedula\":\"invalid-format\"}";
        mockMvc.perform(put("/api/users/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUpdate))
                .andExpect(status().isBadRequest());
    }
}