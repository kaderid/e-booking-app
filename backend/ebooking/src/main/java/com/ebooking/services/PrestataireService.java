package com.ebooking.services;

import com.ebooking.model.Prestataire;
import com.ebooking.repository.PrestataireRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestataireService {

    private final PrestataireRepository prestataireRepository;

    public PrestataireService(PrestataireRepository prestataireRepository) {
        this.prestataireRepository = prestataireRepository;
    }

    public Prestataire savePrestataire(Prestataire prestataire) {
        return prestataireRepository.save(prestataire);
    }

    public List<Prestataire> getAllPrestataires() {
        return prestataireRepository.findAll();
    }
}
