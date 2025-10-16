package com.ebooking.controller;

import com.ebooking.dto.PrestataireRequestDTO;
import com.ebooking.dto.PrestataireResponseDTO;
import com.ebooking.services.PrestataireService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestataires")
@RequiredArgsConstructor
public class PrestataireController {

    private final PrestataireService prestataireService;

    @PostMapping
    public PrestataireResponseDTO createPrestataire(@RequestBody PrestataireRequestDTO dto) {
        return prestataireService.createPrestataire(dto);
    }

    @GetMapping
    public List<PrestataireResponseDTO> getAllPrestataires() {
        return prestataireService.getAllPrestataires();
    }
}

