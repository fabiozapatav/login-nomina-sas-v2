package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private List<User> users = new ArrayList<>(); // Lista mockeada

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        User newUser = new User(1L, "Juan", "TI", "Dev", "123456", 2000.0);
        List<User> newUsers = List.of(newUser);

        // Configurar mocks
        when(users.stream()).thenReturn(Stream.empty());
        when(users.add(any(User.class))).thenReturn(true);

        ResponseEntity<String> response = userController.createUser(newUsers);

        assertEquals(201, response.getStatusCodeValue());
        verify(users).add(newUser); // Verificación con instancia específica
    }

    @Test
    void testDeleteUser_Success() {
        User existingUser = new User(1L, "Juan", "TI", "Dev", "123456", 2000.0);

        // Configurar mocks
        when(users.stream()).thenReturn(Stream.of(existingUser));
        when(users.remove(existingUser)).thenReturn(true);

        ResponseEntity<String> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(users).remove(existingUser);
    }

    @Test
    void testGetUsers_EmptyList() {
        when(users.isEmpty()).thenReturn(true);
        ResponseEntity<List<User>> response = userController.getUsers();
        assertTrue(response.getBody().isEmpty());
    }
}