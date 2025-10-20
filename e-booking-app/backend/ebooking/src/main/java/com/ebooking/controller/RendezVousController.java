package com.ebooking.controller;

import com.ebooking.dto.RendezVousRequestDTO;
import com.ebooking.dto.RendezVousResponseDTO;
import com.ebooking.services.RendezVousService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;

    @PostMapping
    public ResponseEntity<?> createRendezVous(@RequestBody RendezVousRequestDTO dto) {
        try {
            RendezVousResponseDTO response = rendezVousService.createRendezVous(dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public List<RendezVousResponseDTO> getAllRendezVous() {
        return rendezVousService.getAllRendezVous();
    }

    @GetMapping("/client/{clientId}")
    public List<RendezVousResponseDTO> getRendezVousByClientId(@PathVariable Long clientId) {
        return rendezVousService.getRendezVousByClientId(clientId);
    }

    @GetMapping("/prestataire/{prestataireId}")
    public List<RendezVousResponseDTO> getRendezVousByPrestataireId(@PathVariable Long prestataireId) {
        return rendezVousService.getRendezVousByPrestataireId(prestataireId);
    }

    @PutMapping("/{id}")
    public RendezVousResponseDTO updateRendezVous(@PathVariable Long id, @RequestBody RendezVousRequestDTO dto) {
        return rendezVousService.updateRendezVous(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteRendezVous(@PathVariable Long id) {
        rendezVousService.deleteRendezVous(id);
    }
}
