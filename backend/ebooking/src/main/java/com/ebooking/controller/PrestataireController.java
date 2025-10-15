package com.ebooking.controller;

import com.ebooking.model.Prestataire;
import com.ebooking.services.PrestataireService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestataires")
public class PrestataireController {

    private final PrestataireService prestataireService;

    public PrestataireController(PrestataireService prestataireService) {
        this.prestataireService = prestataireService;
    }

    @PostMapping
    public Prestataire createPrestataire(@RequestBody Prestataire prestataire) {
        return prestataireService.savePrestataire(prestataire);
    }

    @GetMapping
    public List<Prestataire> getAllPrestataires() {
        return prestataireService.getAllPrestataires();
    }
}


