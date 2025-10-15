package com.ebooking.controller;

import com.ebooking.model.Disponibilite;
import com.ebooking.services.DisponibiliteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilites")
public class DisponibiliteController {

    private final DisponibiliteService disponibiliteService;

    public DisponibiliteController(DisponibiliteService disponibiliteService) {
        this.disponibiliteService = disponibiliteService;
    }

    @PostMapping
    public Disponibilite create(@RequestBody Disponibilite disponibilite) {
        return disponibiliteService.save(disponibilite);
    }

    @GetMapping
    public List<Disponibilite> getAll() {
        return disponibiliteService.getAll();
    }

    @GetMapping("/{id}")
    public Disponibilite getById(@PathVariable Long id) {
        return disponibiliteService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        disponibiliteService.delete(id);
    }
}
