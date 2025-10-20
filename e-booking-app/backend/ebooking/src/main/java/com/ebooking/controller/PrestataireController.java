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

    @GetMapping("/{id}")
    public PrestataireResponseDTO getPrestataireById(@PathVariable Long id) {
        return prestataireService.getPrestataireById(id);
    }

    @PutMapping("/{id}")
    public PrestataireResponseDTO updatePrestataire(@PathVariable Long id, @RequestBody PrestataireRequestDTO dto) {
        return prestataireService.updatePrestataire(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePrestataire(@PathVariable Long id) {
        prestataireService.deletePrestataire(id);
    }
}

