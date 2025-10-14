package com.ebooking.services;

import com.ebooking.model.RendezVous;
import com.ebooking.repository.RendezVousRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository) {
        this.rendezVousRepository = rendezVousRepository;
    }

    public List<RendezVous> getAll() {
        return rendezVousRepository.findAll();
    }

    public Optional<RendezVous> getById(Long id) {
        return rendezVousRepository.findById(id);
    }

    public RendezVous create(RendezVous rendezVous) {
        return rendezVousRepository.save(rendezVous);
    }

    public void delete(Long id) {
        rendezVousRepository.deleteById(id);
    }
}

