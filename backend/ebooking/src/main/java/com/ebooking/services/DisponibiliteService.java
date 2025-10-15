package com.ebooking.services;

import com.ebooking.model.Disponibilite;
import com.ebooking.repository.DisponibiliteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DisponibiliteService {

    private final DisponibiliteRepository disponibiliteRepository;

    public DisponibiliteService(DisponibiliteRepository disponibiliteRepository) {
        this.disponibiliteRepository = disponibiliteRepository;
    }

    public Disponibilite save(Disponibilite disponibilite) {
        return disponibiliteRepository.save(disponibilite);
    }

    public List<Disponibilite> getAll() {
        return disponibiliteRepository.findAll();
    }

    public Disponibilite getById(Long id) {
        return disponibiliteRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        disponibiliteRepository.deleteById(id);
    }
}

