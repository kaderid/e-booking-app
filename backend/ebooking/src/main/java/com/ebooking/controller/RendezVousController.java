package com.ebooking.controller;

import com.ebooking.dto.RendezVousRequestDTO;
import com.ebooking.dto.RendezVousResponseDTO;
import com.ebooking.services.RendezVousService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;

    @PostMapping
    public RendezVousResponseDTO createRendezVous(@RequestBody RendezVousRequestDTO dto) {
        return rendezVousService.createRendezVous(dto);
    }

    @GetMapping
    public List<RendezVousResponseDTO> getAllRendezVous() {
        return rendezVousService.getAllRendezVous();
    }
}
