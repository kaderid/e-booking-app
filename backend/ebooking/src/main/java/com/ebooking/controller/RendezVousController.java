package com.ebooking.controller;

import com.ebooking.model.RendezVous;
import com.ebooking.services.RendezVousService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    @PostMapping
    public RendezVous createRendezVous(@RequestBody RendezVous rendezVous) {
        return rendezVousService.saveRendezVous(rendezVous);
    }

    @GetMapping
    public List<RendezVous> getAllRendezVous() {
        return rendezVousService.getAllRendezVous();
    }
}
