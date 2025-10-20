package com.ebooking.services;

import com.ebooking.dto.UserRequestDTO;
import com.ebooking.dto.UserResponseDTO;
import com.ebooking.model.Statut;
import com.ebooking.model.User;
import com.ebooking.repository.UserRepository;
import com.ebooking.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public String register(UserRequestDTO dto) {
        // Create user via UserService
        UserResponseDTO userResponse = userService.createUser(dto);

        // Generate JWT token with role and user ID
        return jwtService.generateToken(dto.getEmail(), dto.getRole(), userResponse.getId());
    }

    public String login(String email, String motDePasse) {
        // Get user from database to check status before authentication
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if account is blocked
        if (user.getStatut() == Statut.BLOQUE) {
            throw new RuntimeException("ACCOUNT_BLOCKED:Votre compte a été bloqué par l'administrateur. Veuillez contacter le support client pour plus d'informations.");
        }

        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, motDePasse)
        );

        // Generate JWT token with role and user ID
        return jwtService.generateToken(email, user.getRole().toString(), user.getId());
    }
}
