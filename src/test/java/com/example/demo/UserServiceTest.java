package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        userService = new UserService(users);
    }

    @Test
    void createUser_ValidUser_Success() {
        User user = new User(1L, "Ana", "TI", "Dev", "112233", 3000.0);
        User result = userService.createUser(user);
        assertEquals(1, users.size());
        assertEquals("Ana", result.getName());
    }

    @Test
    void createUser_DuplicateId_ThrowsException() {
        User user1 = new User(1L, "Ana", "TI", "Dev", "112233", 3000.0);
        userService.createUser(user1);

        User user2 = new User(1L, "Juan", "RH", "Manager", "445566", 4000.0);
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user2));
    }

    @Test
    void updateUser_ValidData_Success() {
        User original = new User(1L, "Ana", "TI", "Dev", "112233", 3000.0);
        users.add(original);

        User updated = new User(1L, "Ana Updated", "Marketing", "Senior", "112233", 3500.0);
        User result = userService.updateUser(1L, updated);

        assertEquals("Ana Updated", result.getName());
        assertEquals("Marketing", result.getArea());
    }

    @Test
    void deleteUser_ExistingUser_Success() {
        User user = new User(1L, "Ana", "TI", "Dev", "112233", 3000.0);
        users.add(user);
        userService.deleteUser(1L);
        assertTrue(users.isEmpty());
    }
}