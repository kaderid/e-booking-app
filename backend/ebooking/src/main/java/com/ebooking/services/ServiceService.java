package com.ebooking.services;

import com.ebooking.exception.ResourceAlreadyExistsException;
import com.ebooking.model.ServiceEntity;
import com.ebooking.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<ServiceEntity> getAll() {
        return serviceRepository.findAll();
    }

    public Optional<ServiceEntity> getById(Long id) {
        return serviceRepository.findById(id);
    }

    public ServiceEntity create(ServiceEntity service) {
        if (serviceRepository.findByNom(service.getNom()).isPresent()) {
            throw new ResourceAlreadyExistsException("Le service '" + service.getNom() + "' existe déjà.");
        }
        return serviceRepository.save(service);
    }

    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }
}
