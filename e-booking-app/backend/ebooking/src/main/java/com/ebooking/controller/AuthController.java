package com.ebooking.controller;

import com.ebooking.dto.UserRequestDTO;
import com.ebooking.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserRequestDTO dto) {
        String token = authService.register(dto);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "User registered successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest.getEmail(), loginRequest.getMotDePasse());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();

            // Check if account is blocked
            if (e.getMessage().startsWith("ACCOUNT_BLOCKED:")) {
                String message = e.getMessage().substring("ACCOUNT_BLOCKED:".length());
                errorResponse.put("error", "ACCOUNT_BLOCKED");
                errorResponse.put("message", message);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            // Other authentication errors
            errorResponse.put("error", "AUTHENTICATION_FAILED");
            errorResponse.put("message", "Email ou mot de passe incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}

class LoginRequest {
    private String email;
    private String motDePasse;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
