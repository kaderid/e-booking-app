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

    public PrestataireResponseDTO getPrestataireById(Long id) {
        Prestataire prestataire = prestataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));
        return prestataireMapper.toResponse(prestataire);
    }

    public PrestataireResponseDTO updatePrestataire(Long id, PrestataireRequestDTO dto) {
        Prestataire prestataire = prestataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        if (dto.getSpecialite() != null) {
            prestataire.setSpecialite(dto.getSpecialite());
        }
        if (dto.getAdresse() != null) {
            prestataire.setAdresse(dto.getAdresse());
        }
        if (dto.getServiceId() != null) {
            ServiceEntity service = serviceRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service introuvable"));
            prestataire.setCategorieService(service);
        }

        Prestataire updated = prestataireRepository.save(prestataire);
        return prestataireMapper.toResponse(updated);
    }

    public void deletePrestataire(Long id) {
        if (!prestataireRepository.existsById(id)) {
            throw new RuntimeException("Prestataire introuvable");
        }
        prestataireRepository.deleteById(id);
    }
}
