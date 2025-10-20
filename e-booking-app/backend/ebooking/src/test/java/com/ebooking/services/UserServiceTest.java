package com.ebooking.services;

import com.ebooking.dto.UserRequestDTO;
import com.ebooking.dto.UserResponseDTO;
import com.ebooking.mapper.UserMapper;
import com.ebooking.model.Role;
import com.ebooking.model.Statut;
import com.ebooking.model.User;
import com.ebooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserRequestDTO testUserRequest;
    private UserResponseDTO testUserResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .prenom("John")
                .nom("Doe")
                .email("john.doe@example.com")
                .telephone("+221771234567")
                .motDePasse("password123")
                .role(Role.CLIENT)
                .statut(Statut.ACTIF)
                .build();

        testUserRequest = new UserRequestDTO();
        testUserRequest.setPrenom("John");
        testUserRequest.setNom("Doe");
        testUserRequest.setEmail("john.doe@example.com");
        testUserRequest.setTelephone("+221771234567");
        testUserRequest.setMotDePasse("password123");
        testUserRequest.setRole("CLIENT");

        testUserResponse = UserResponseDTO.builder()
                .id(1L)
                .prenom("John")
                .nom("Doe")
                .email("john.doe@example.com")
                .telephone("+221771234567")
                .role("CLIENT")
                .statut("ACTIF")
                .build();
    }

    @Test
    void testCreateUser_Success() {
        // Given
        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toResponse(any(User.class))).thenReturn(testUserResponse);

        // When
        UserResponseDTO result = userService.createUser(testUserRequest);

        // Then
        assertNotNull(result);
        assertEquals("John", result.getPrenom());
        assertEquals("Doe", result.getNom());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetAllUsers_Success() {
        // Given
        User user2 = User.builder().id(2L).prenom("Jane").nom("Doe").build();
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));
        when(userMapper.toResponse(any(User.class))).thenReturn(testUserResponse);

        // When
        List<UserResponseDTO> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toResponse(any(User.class))).thenReturn(testUserResponse);

        // When
        UserResponseDTO result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            userService.getUserById(999L);
        });
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toResponse(any(User.class))).thenReturn(testUserResponse);

        UserRequestDTO updateRequest = new UserRequestDTO();
        updateRequest.setPrenom("Johnny");

        // When
        UserResponseDTO result = userService.updateUser(1L, updateRequest);

        // Then
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).deleteById(1L);
    }
}
