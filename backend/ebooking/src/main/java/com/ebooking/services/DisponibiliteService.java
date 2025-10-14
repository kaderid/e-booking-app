package com.ebooking.services;

import com.ebooking.model.Disponibilite;
import com.ebooking.repository.DisponibiliteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisponibiliteService {

    private final DisponibiliteRepository disponibiliteRepository;

    public DisponibiliteService(DisponibiliteRepository disponibiliteRepository) {
        this.disponibiliteRepository = disponibiliteRepository;
    }

    public List<Disponibilite> getAll() {
        return disponibiliteRepository.findAll();
    }

    public Optional<Disponibilite> getById(Long id) {
        return disponibiliteRepository.findById(id);
    }

    public Disponibilite create(Disponibilite disponibilite) {
        return disponibiliteRepository.save(disponibilite);
    }

    public void delete(Long id) {
        disponibiliteRepository.deleteById(id);
    }
}

