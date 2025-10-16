package com.ebooking.services;

import com.ebooking.dto.RendezVousRequestDTO;
import com.ebooking.dto.RendezVousResponseDTO;
import com.ebooking.mapper.RendezVousMapper;
import com.ebooking.model.Prestataire;
import com.ebooking.model.RendezVous;
import com.ebooking.model.User;
import com.ebooking.repository.PrestataireRepository;
import com.ebooking.repository.RendezVousRepository;
import com.ebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final RendezVousMapper rendezVousMapper;
    private final UserRepository userRepository;
    private final PrestataireRepository prestataireRepository;

    public RendezVousResponseDTO createRendezVous(RendezVousRequestDTO dto) {
        User client = userRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));
        Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        boolean exists = rendezVousRepository.findExistingRendezVous(
                prestataire.getId(), dto.getDate(), dto.getHeure()
        ).isPresent();

        if (exists) throw new IllegalArgumentException("⚠️ Le prestataire a déjà un rendez-vous à cette heure");

        RendezVous entity = rendezVousMapper.toEntity(dto, client, prestataire);
        RendezVous saved = rendezVousRepository.save(entity);
        return rendezVousMapper.toResponse(saved);
    }

    public List<RendezVousResponseDTO> getAllRendezVous() {
        return rendezVousRepository.findAll()
                .stream()
                .map(rendezVousMapper::toResponse)
                .collect(Collectors.toList());
    }
}
