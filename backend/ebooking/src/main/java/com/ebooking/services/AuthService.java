package com.ebooking.services;

import com.ebooking.model.User;
import com.ebooking.repository.UserRepository;
import com.ebooking.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(User user) {
        user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
        userRepository.save(user);
        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(password, user.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }
}

