package com.ebooking.services;

import com.ebooking.model.Prestataire;
import com.ebooking.repository.PrestataireRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrestataireService {

    private final PrestataireRepository prestataireRepository;

    public PrestataireService(PrestataireRepository prestataireRepository) {
        this.prestataireRepository = prestataireRepository;
    }

    public List<Prestataire> getAll() {
        return prestataireRepository.findAll();
    }

    public Optional<Prestataire> getById(Long id) {
        return prestataireRepository.findById(id);
    }

    public Prestataire create(Prestataire prestataire) {
        return prestataireRepository.save(prestataire);
    }

    public void delete(Long id) {
        prestataireRepository.deleteById(id);
    }
}
