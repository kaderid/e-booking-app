package com.ebooking.controller;

import com.ebooking.dto.ServiceRequestDTO;
import com.ebooking.dto.ServiceResponseDTO;
import com.ebooking.services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceEntityController {

    private final ServiceService serviceService;

    @PostMapping
    public ServiceResponseDTO createService(@RequestBody ServiceRequestDTO dto) {
        return serviceService.create(dto);
    }

    @GetMapping
    public List<ServiceResponseDTO> getAllServices() {
        return serviceService.getAll();
    }

    @GetMapping("/{id}")
    public ServiceResponseDTO getServiceById(@PathVariable Long id) {
        return serviceService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable Long id) {
        serviceService.delete(id);
    }
}
