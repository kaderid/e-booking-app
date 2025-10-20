package com.ebooking.controller;

import com.ebooking.dto.DisponibiliteRequestDTO;
import com.ebooking.dto.DisponibiliteResponseDTO;
import com.ebooking.services.DisponibiliteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilites")
@RequiredArgsConstructor
public class DisponibiliteController {

    private final DisponibiliteService disponibiliteService;

    @PostMapping
    public DisponibiliteResponseDTO createDisponibilite(@RequestBody DisponibiliteRequestDTO dto) {
        return disponibiliteService.createDisponibilite(dto);
    }

    @GetMapping
    public List<DisponibiliteResponseDTO> getAllDisponibilites() {
        return disponibiliteService.getAllDisponibilites();
    }

    @GetMapping("/{prestataireId}")
    public List<DisponibiliteResponseDTO> getDisponibilitesByPrestataireId(@PathVariable Long prestataireId) {
        return disponibiliteService.getDisponibilitesByPrestataireId(prestataireId);
    }

    @PutMapping("/{id}")
    public DisponibiliteResponseDTO updateDisponibilite(@PathVariable Long id, @RequestBody DisponibiliteRequestDTO dto) {
        return disponibiliteService.updateDisponibilite(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteDisponibilite(@PathVariable Long id) {
        disponibiliteService.deleteDisponibilite(id);
    }
}
