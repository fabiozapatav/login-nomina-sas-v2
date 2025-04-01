package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
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
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(delete("/api/users/all")); // Limpia los usuarios antes de cada prueba
    }

    // Añadir estos tests a la clase existente

    @Test
    void testGetUsers_WithData() throws Exception {
        // Crear usuario
        String userJson = "[{\"id\":4,\"name\":\"Laura\",\"area\":\"Finanzas\",\"cargo\":\"Analista\",\"cedula\":\"998877\",\"salary\":2800.0}]";
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(userJson));

        // Obtener usuarios
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laura"))
                .andExpect(jsonPath("$[0].salary").value(2800.0));
    }

    @Test
    void testUpdateUser_InvalidData() throws Exception {
        String invalidUserJson = "{\"name\":\"\"}"; // Nombre vacío
        mockMvc.perform(put("/api/users/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUser_InvalidPayload() throws Exception {
        String invalidJson = "[{\"id\":null,\"name\":\"\"}]"; // ID nulo y nombre vacío
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUsers_EmptyList() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]")); // Lista vacía esperada
    }

    @Test
    void testCreateUser() throws Exception {
        String userJson = "[{\"id\":1,\"name\":\"Juan\",\"area\":\"TI\",\"cargo\":\"Dev\",\"cedula\":\"123456\",\"salary\":2000.0}]";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuarios creados correctamente."));
    }

    @Test
    void testCreateUser_Conflict() throws Exception {
        String userJson = "[{\"id\":1,\"name\":\"Juan\",\"area\":\"TI\",\"cargo\":\"Dev\",\"cedula\":\"123456\",\"salary\":2000.0}]";

        // Crear el usuario por primera vez
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        // Intentar crearlo nuevamente con el mismo ID
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(content().string("El ID 1 ya está registrado o es inválido."));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario con ID 99 no encontrado."));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        String userJson = "[{\"id\":2,\"name\":\"Maria\",\"area\":\"RH\",\"cargo\":\"Manager\",\"cedula\":\"654321\",\"salary\":3000.0}]";

        // Crear el usuario antes de eliminarlo
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        // Eliminar el usuario creado
        mockMvc.perform(delete("/api/users/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario con ID 2 eliminado correctamente."));
    }
}