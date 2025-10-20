package com.ebooking.services;

import com.ebooking.dto.DisponibiliteRequestDTO;
import com.ebooking.dto.DisponibiliteResponseDTO;
import com.ebooking.mapper.DisponibiliteMapper;
import com.ebooking.model.Disponibilite;
import com.ebooking.model.Prestataire;
import com.ebooking.repository.DisponibiliteRepository;
import com.ebooking.repository.PrestataireRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibiliteService {

    private final DisponibiliteRepository disponibiliteRepository;
    private final DisponibiliteMapper disponibiliteMapper;
    private final PrestataireRepository prestataireRepository;

    public DisponibiliteService(DisponibiliteRepository disponibiliteRepository,
                                DisponibiliteMapper disponibiliteMapper,
                                PrestataireRepository prestataireRepository) {
        this.disponibiliteRepository = disponibiliteRepository;
        this.disponibiliteMapper = disponibiliteMapper;
        this.prestataireRepository = prestataireRepository;
    }

    public List<DisponibiliteResponseDTO> getAllDisponibilites() {
        return disponibiliteRepository.findAll()
                .stream()
                .map(disponibiliteMapper::toDto)
                .collect(Collectors.toList());
    }

    public DisponibiliteResponseDTO createDisponibilite(DisponibiliteRequestDTO dto) {
        Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        // Validation: l'heure de fin doit être après l'heure de début
        if (dto.getHeureFin().isBefore(dto.getHeureDebut()) || dto.getHeureFin().equals(dto.getHeureDebut())) {
            throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début");
        }

        Disponibilite disponibilite = Disponibilite.builder()
                .prestataire(prestataire)
                .jourSemaine(dto.getJourSemaine())
                .heureDebut(dto.getHeureDebut())
                .heureFin(dto.getHeureFin())
                .build();

        return disponibiliteMapper.toDto(disponibiliteRepository.save(disponibilite));
    }

    public List<DisponibiliteResponseDTO> getDisponibilitesByPrestataireId(Long prestataireId) {
        return disponibiliteRepository.findByPrestataireId(prestataireId)
                .stream()
                .map(disponibiliteMapper::toDto)
                .collect(Collectors.toList());
    }

    public DisponibiliteResponseDTO updateDisponibilite(Long id, DisponibiliteRequestDTO dto) {
        Disponibilite disponibilite = disponibiliteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilité introuvable"));

        if (dto.getJourSemaine() != null) {
            disponibilite.setJourSemaine(dto.getJourSemaine());
        }
        if (dto.getHeureDebut() != null) {
            disponibilite.setHeureDebut(dto.getHeureDebut());
        }
        if (dto.getHeureFin() != null) {
            disponibilite.setHeureFin(dto.getHeureFin());
        }

        Disponibilite updated = disponibiliteRepository.save(disponibilite);
        return disponibiliteMapper.toDto(updated);
    }

    public void deleteDisponibilite(Long id) {
        if (!disponibiliteRepository.existsById(id)) {
            throw new RuntimeException("Disponibilité introuvable");
        }
        disponibiliteRepository.deleteById(id);
    }
}
