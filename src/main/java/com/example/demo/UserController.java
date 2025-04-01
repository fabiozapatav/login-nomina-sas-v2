package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    private List<User> users = new ArrayList<>(); // Eliminamos 'final' para permitir inyección de mocks

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(users);
    }

    // Crear usuarios
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody List<User> newUsers) {
        StringBuilder conflictMessage = new StringBuilder();

        for (User newUser : newUsers) {
            if (newUser.getId() == null || users.stream().anyMatch(u -> u.getId().equals(newUser.getId()))) {
                conflictMessage.append("El ID ").append(newUser.getId()).append(" ya está registrado o es inválido. ");
                continue;
            }
            if (newUser.getName() == null || newUser.getName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El campo 'name' no puede estar vacío.");
            }
            users.add(newUser);
        }

        if (conflictMessage.length() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictMessage.toString().trim());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuarios creados correctamente.");
    }

    // Eliminar usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> userToRemove = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        if (userToRemove.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con ID " + id + " no encontrado.");
        }

        users.remove(userToRemove.get());
        return ResponseEntity.ok("Usuario con ID " + id + " eliminado correctamente.");
    }

    // Eliminar todos los usuarios
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllUsers() {
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No hay usuarios para eliminar.");
        }
        users.clear();
        return ResponseEntity.ok("Todos los usuarios han sido eliminados.");
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existingUser = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con ID " + id + " no encontrado.");
        }

        if (updatedUser.getName() == null || updatedUser.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El campo 'name' no puede estar vacío.");
        }

        User user = existingUser.get();
        user.setName(updatedUser.getName());
        user.setArea(updatedUser.getArea());
        user.setCargo(updatedUser.getCargo());
        user.setCedula(updatedUser.getCedula());
        user.setSalary(updatedUser.getSalary());

        return ResponseEntity.ok("Usuario con ID " + id + " actualizado correctamente.");
    }
}