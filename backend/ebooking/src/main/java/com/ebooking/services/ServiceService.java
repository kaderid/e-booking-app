package com.ebooking.services;

import com.ebooking.dto.ServiceRequestDTO;
import com.ebooking.dto.ServiceResponseDTO;
import com.ebooking.model.ServiceEntity;
import com.ebooking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceResponseDTO create(ServiceRequestDTO dto) {
        ServiceEntity service = ServiceEntity.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .build();

        ServiceEntity saved = serviceRepository.save(service);
        return mapToResponse(saved);
    }

    public List<ServiceResponseDTO> getAll() {
        return serviceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ServiceResponseDTO getById(Long id) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service non trouvé avec l’ID : " + id));
        return mapToResponse(service);
    }

    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }

    private ServiceResponseDTO mapToResponse(ServiceEntity service) {
        return ServiceResponseDTO.builder()
                .id(service.getId())
                .nom(service.getNom())
                .description(service.getDescription())
                .build();
    }
}
