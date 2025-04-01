package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final List<User> users;

    public UserService(List<User> users) {
        this.users = users;
    }

    // Versión corregida
    public User createUser(User user) {
        if (user.getId() == null || users.stream().anyMatch(u -> u.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("ID inválido o duplicado");
        }
        users.add(user);
        return user;
    }

    public User updateUser(Long id, User updatedUser) {
        User existing = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        existing.setName(updatedUser.getName());
        existing.setArea(updatedUser.getArea());
        existing.setCargo(updatedUser.getCargo());
        existing.setCedula(updatedUser.getCedula());
        existing.setSalary(updatedUser.getSalary());
        return existing;
    }

    public void deleteUser(Long id) {
        if(!users.removeIf(u -> u.getId().equals(id))) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
    }
}