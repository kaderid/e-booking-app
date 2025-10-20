package com.ebooking.services;

import com.ebooking.dto.RendezVousRequestDTO;
import com.ebooking.dto.RendezVousResponseDTO;
import com.ebooking.mapper.RendezVousMapper;
import com.ebooking.model.*;
import com.ebooking.repository.DisponibiliteRepository;
import com.ebooking.repository.PrestataireRepository;
import com.ebooking.repository.RendezVousRepository;
import com.ebooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RendezVousServiceTest {

    @Mock
    private RendezVousRepository rendezVousRepository;

    @Mock
    private RendezVousMapper rendezVousMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PrestataireRepository prestataireRepository;

    @Mock
    private DisponibiliteRepository disponibiliteRepository;

    @InjectMocks
    private RendezVousService rendezVousService;

    private User client;
    private Prestataire prestataire;
    private RendezVous rendezVous;
    private RendezVousRequestDTO requestDTO;
    private RendezVousResponseDTO responseDTO;
    private Disponibilite disponibilite;

    @BeforeEach
    void setUp() {
        client = User.builder()
                .id(1L)
                .prenom("Client")
                .nom("Test")
                .email("client@test.com")
                .role(Role.CLIENT)
                .build();

        prestataire = Prestataire.builder()
                .id(1L)
                .specialite("Medecin generaliste")
                .adresse("Dakar")
                .build();

        disponibilite = Disponibilite.builder()
                .id(1L)
                .prestataire(prestataire)
                .jourSemaine("Lundi")
                .heureDebut(LocalTime.of(9, 0))
                .heureFin(LocalTime.of(17, 0))
                .build();

        // Définir une date qui tombe un lundi (2025-10-20 est un lundi)
        LocalDate lundi = LocalDate.of(2025, 10, 20);

        rendezVous = RendezVous.builder()
                .id(1L)
                .client(client)
                .prestataire(prestataire)
                .date(lundi)
                .heure(LocalTime.of(10, 0))
                .statut(StatutRendezVous.EN_ATTENTE)
                .build();

        requestDTO = RendezVousRequestDTO.builder()
                .clientId(1L)
                .prestataireId(1L)
                .date(lundi)
                .heure(LocalTime.of(10, 0))
                .build();

        responseDTO = new RendezVousResponseDTO();
        responseDTO.setId(1L);
    }

    @Test
    void testCreateRendezVous_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(prestataireRepository.findById(1L)).thenReturn(Optional.of(prestataire));
        when(disponibiliteRepository.findByPrestataireId(1L)).thenReturn(Arrays.asList(disponibilite));
        when(rendezVousRepository.findExistingRendezVous(any(), any(), any())).thenReturn(Optional.empty());
        when(rendezVousMapper.toEntity(any(), any(), any())).thenReturn(rendezVous);
        when(rendezVousRepository.save(any())).thenReturn(rendezVous);
        when(rendezVousMapper.toResponse(any())).thenReturn(responseDTO);

        // When
        RendezVousResponseDTO result = rendezVousService.createRendezVous(requestDTO);

        // Then
        assertNotNull(result);
        verify(rendezVousRepository).save(any());
    }

    @Test
    void testCreateRendezVous_PastDate() {
        // Given
        requestDTO.setDate(LocalDate.now().minusDays(1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(prestataireRepository.findById(1L)).thenReturn(Optional.of(prestataire));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            rendezVousService.createRendezVous(requestDTO);
        });
    }

    @Test
    void testCreateRendezVous_PrestataireNotAvailable() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(prestataireRepository.findById(1L)).thenReturn(Optional.of(prestataire));
        when(disponibiliteRepository.findByPrestataireId(1L)).thenReturn(Arrays.asList());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            rendezVousService.createRendezVous(requestDTO);
        });
    }

    @Test
    void testCreateRendezVous_AlreadyBooked() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(prestataireRepository.findById(1L)).thenReturn(Optional.of(prestataire));
        when(disponibiliteRepository.findByPrestataireId(1L)).thenReturn(Arrays.asList(disponibilite));
        when(rendezVousRepository.findExistingRendezVous(any(), any(), any())).thenReturn(Optional.of(rendezVous));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rendezVousService.createRendezVous(requestDTO);
        });

        assertEquals("Le prestataire a déjà un rendez-vous à cette heure", exception.getMessage());
    }

    @Test
    void testDeleteRendezVous_Success() {
        // Given
        when(rendezVousRepository.existsById(1L)).thenReturn(true);

        // When
        rendezVousService.deleteRendezVous(1L);

        // Then
        verify(rendezVousRepository).deleteById(1L);
    }

    @Test
    void testDeleteRendezVous_NotFound() {
        // Given
        when(rendezVousRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            rendezVousService.deleteRendezVous(999L);
        });
    }
}
