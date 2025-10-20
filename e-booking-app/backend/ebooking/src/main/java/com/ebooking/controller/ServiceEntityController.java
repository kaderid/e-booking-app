package com.ebooking.controller;

import com.ebooking.dto.ServiceRequestDTO;
import com.ebooking.dto.ServiceResponseDTO;
import com.ebooking.mapper.ServiceMapper;
import com.ebooking.model.ServiceEntity;
import com.ebooking.services.ServiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceEntityController {

    private final ServiceService serviceService;
    private final ServiceMapper serviceMapper;

    public ServiceEntityController(ServiceService serviceService, ServiceMapper serviceMapper) {
        this.serviceService = serviceService;
        this.serviceMapper = serviceMapper;
    }

    @PostMapping
    public ServiceResponseDTO create(@RequestBody ServiceRequestDTO dto) {
        // 🔄 conversion du DTO → Entité avec MapStruct
        ServiceEntity entity = serviceMapper.toEntity(dto);

        // 💾 création en base
        ServiceEntity saved = serviceService.create(entity, dto.getPrestataireId());

        // 🔁 conversion Entité → DTO pour la réponse
        return serviceMapper.toResponseDTO(saved);
    }

    @GetMapping
    public List<ServiceResponseDTO> getAll() {
        return serviceService.getAll().stream()
                .map(serviceMapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/prestataire/{prestataireId}")
    public List<ServiceResponseDTO> getServicesByPrestataire(@PathVariable Long prestataireId) {
        return serviceService.getServicesByPrestataireId(prestataireId).stream()
                .map(serviceMapper::toResponseDTO)
                .toList();
    }

    @PutMapping("/{id}")
    public ServiceResponseDTO update(@PathVariable Long id, @RequestBody ServiceRequestDTO dto) {
        ServiceEntity entity = serviceMapper.toEntity(dto);
        ServiceEntity updated = serviceService.update(id, entity, dto.getPrestataireId());
        return serviceMapper.toResponseDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        serviceService.delete(id);
    }
}
