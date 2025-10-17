package com.ebooking.services;

import com.ebooking.dto.DisponibiliteRequestDTO;
import com.ebooking.dto.DisponibiliteResponseDTO;
import com.ebooking.mapper.DisponibiliteMapper;
import com.ebooking.model.Disponibilite;
import com.ebooking.repository.DisponibiliteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibiliteService {

    private final DisponibiliteRepository disponibiliteRepository;
    private final DisponibiliteMapper disponibiliteMapper;

    public DisponibiliteService(DisponibiliteRepository disponibiliteRepository,
                                DisponibiliteMapper disponibiliteMapper) {
        this.disponibiliteRepository = disponibiliteRepository;
        this.disponibiliteMapper = disponibiliteMapper;
    }

    public List<DisponibiliteResponseDTO> getAllDisponibilites() {
        return disponibiliteRepository.findAll()
                .stream()
                .map(disponibiliteMapper::toDto)
                .collect(Collectors.toList());
    }

    public DisponibiliteResponseDTO createDisponibilite(DisponibiliteRequestDTO dto) {
        Disponibilite disponibilite = disponibiliteMapper.toEntity(dto);
        return disponibiliteMapper.toDto(disponibiliteRepository.save(disponibilite));
    }
}
