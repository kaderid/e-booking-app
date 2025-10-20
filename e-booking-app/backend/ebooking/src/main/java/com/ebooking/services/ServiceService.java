package com.ebooking.services;

import com.ebooking.exception.ResourceAlreadyExistsException;
import com.ebooking.exception.ResourceNotFoundException;
import com.ebooking.model.Prestataire;
import com.ebooking.model.ServiceEntity;
import com.ebooking.repository.PrestataireRepository;
import com.ebooking.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final PrestataireRepository prestataireRepository;

    public ServiceService(ServiceRepository serviceRepository, PrestataireRepository prestataireRepository) {
        this.serviceRepository = serviceRepository;
        this.prestataireRepository = prestataireRepository;
    }

    public List<ServiceEntity> getAll() {
        return serviceRepository.findAll();
    }

    public Optional<ServiceEntity> getById(Long id) {
        return serviceRepository.findById(id);
    }

    public List<ServiceEntity> getServicesByPrestataireId(Long prestataireId) {
        return serviceRepository.findByPrestataireId(prestataireId);
    }

    public ServiceEntity create(ServiceEntity service, Long prestataireId) {
        if (prestataireId != null) {
            Prestataire prestataire = prestataireRepository.findById(prestataireId)
                    .orElseThrow(() -> new ResourceNotFoundException("Prestataire non trouvé"));
            service.setPrestataire(prestataire);
        }
        return serviceRepository.save(service);
    }

    public ServiceEntity update(Long id, ServiceEntity service, Long prestataireId) {
        ServiceEntity existing = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé"));

        existing.setNom(service.getNom());
        existing.setDescription(service.getDescription());
        existing.setDuree(service.getDuree());
        existing.setPrix(service.getPrix());

        if (prestataireId != null) {
            Prestataire prestataire = prestataireRepository.findById(prestataireId)
                    .orElseThrow(() -> new ResourceNotFoundException("Prestataire non trouvé"));
            existing.setPrestataire(prestataire);
        }

        return serviceRepository.save(existing);
    }

    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }
}
