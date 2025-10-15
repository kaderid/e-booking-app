package com.ebooking.services;

import com.ebooking.model.RendezVous;
import com.ebooking.repository.RendezVousRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository) {
        this.rendezVousRepository = rendezVousRepository;
    }

    public RendezVous saveRendezVous(RendezVous rendezVous) {
        boolean exists = rendezVousRepository.findExistingRendezVous(
                rendezVous.getPrestataire().getId(),
                rendezVous.getDate(),
                rendezVous.getHeure()
        ).isPresent();

        if (exists) {
            throw new IllegalArgumentException("⚠️ Le prestataire a déjà un rendez-vous à cette date et heure !");
        }

        return rendezVousRepository.save(rendezVous);
    }

    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }
}
