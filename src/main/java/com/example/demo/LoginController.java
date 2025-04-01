package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen
public class LoginController {

    private final Map<String, String> usuarios = new HashMap<>();

    public LoginController() {
        usuarios.put("admin", "1234");
        usuarios.put("usuario", "password");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (usuarios.containsKey(username) && usuarios.get(username).equals(password)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login exitoso");
            response.put("token", "1234567890abcdef"); // Simulación de un token de autenticación
            return ResponseEntity.ok(response);
        }

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "Credenciales incorrectas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

}