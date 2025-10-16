package com.ebooking.services;

import com.ebooking.dto.DisponibiliteRequestDTO;
import com.ebooking.dto.DisponibiliteResponseDTO;
import com.ebooking.mapper.DisponibiliteMapper;
import com.ebooking.model.Disponibilite;
import com.ebooking.model.Prestataire;
import com.ebooking.repository.DisponibiliteRepository;
import com.ebooking.repository.PrestataireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisponibiliteService {

    private final DisponibiliteRepository disponibiliteRepository;
    private final PrestataireRepository prestataireRepository;
    private final DisponibiliteMapper disponibiliteMapper;

    public DisponibiliteResponseDTO createDisponibilite(DisponibiliteRequestDTO dto) {
        Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        Disponibilite entity = disponibiliteMapper.toEntity(dto, prestataire);
        Disponibilite saved = disponibiliteRepository.save(entity);

        return disponibiliteMapper.toResponse(saved);
    }

    public List<DisponibiliteResponseDTO> getAllDisponibilites() {
        return disponibiliteRepository.findAll()
                .stream()
                .map(disponibiliteMapper::toResponse)
                .collect(Collectors.toList());
    }
}
