package com.ebooking.services;

import com.ebooking.dto.PrestataireRequestDTO;
import com.ebooking.dto.PrestataireResponseDTO;
import com.ebooking.mapper.PrestataireMapper;
import com.ebooking.model.Prestataire;
import com.ebooking.model.ServiceEntity;
import com.ebooking.model.User;
import com.ebooking.repository.PrestataireRepository;
import com.ebooking.repository.ServiceRepository;
import com.ebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestataireService {

    private final PrestataireRepository prestataireRepository;
    private final PrestataireMapper prestataireMapper;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    public PrestataireResponseDTO createPrestataire(PrestataireRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        ServiceEntity service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service introuvable"));

        Prestataire prestataire = prestataireMapper.toEntity(dto, user, service);
        Prestataire saved = prestataireRepository.save(prestataire);

        return prestataireMapper.toResponse(saved);
    }

    public List<PrestataireResponseDTO> getAllPrestataires() {
        return prestataireRepository.findAll()
                .stream()
                .map(prestataireMapper::toResponse)
                .collect(Collectors.toList());
    }
}
