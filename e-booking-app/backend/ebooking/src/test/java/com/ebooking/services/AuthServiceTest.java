package com.ebooking.services;

import com.ebooking.dto.UserRequestDTO;
import com.ebooking.dto.UserResponseDTO;
import com.ebooking.model.Role;
import com.ebooking.model.Statut;
import com.ebooking.model.User;
import com.ebooking.repository.UserRepository;
import com.ebooking.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private UserRequestDTO testUserRequestDTO;
    private UserResponseDTO testUserResponseDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .prenom("Test")
                .nom("User")
                .email("test@example.com")
                .telephone("+221771234567")
                .motDePasse("encodedPassword")
                .role(Role.CLIENT)
                .statut(Statut.ACTIF)
                .build();

        testUserRequestDTO = new UserRequestDTO();
        testUserRequestDTO.setPrenom("Test");
        testUserRequestDTO.setNom("User");
        testUserRequestDTO.setEmail("test@example.com");
        testUserRequestDTO.setTelephone("+221771234567");
        testUserRequestDTO.setMotDePasse("password");
        testUserRequestDTO.setRole("CLIENT");

        testUserResponseDTO = UserResponseDTO.builder()
                .id(1L)
                .prenom("Test")
                .nom("User")
                .email("test@example.com")
                .telephone("+221771234567")
                .role("CLIENT")
                .statut("ACTIF")
                .build();
    }

    @Test
    void testRegister_Success() {
        // Given
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(testUserResponseDTO);
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("jwt-token");

        // When
        String token = authService.register(testUserRequestDTO);

        // Then
        assertNotNull(token);
        assertEquals("jwt-token", token);
        verify(userService).createUser(any(UserRequestDTO.class));
        verify(jwtService).generateToken("test@example.com", "CLIENT", 1L);
    }

    @Test
    void testLogin_Success() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(anyString(), anyString(), any())).thenReturn("jwt-token");

        // When
        String token = authService.login("test@example.com", "password");

        // Then
        assertNotNull(token);
        assertEquals("jwt-token", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("test@example.com");
        verify(jwtService).generateToken("test@example.com", "CLIENT", 1L);
    }

    @Test
    void testLogin_UserNotFound() {
        // Given
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authService.login("unknown@example.com", "password");
        });
    }
}
