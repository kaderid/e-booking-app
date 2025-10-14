package com.ebooking.controller;

import com.ebooking.model.ServiceEntity;
import com.ebooking.services.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceEntityController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceEntityController.class);

    private final ServiceService serviceService;

    public ServiceEntityController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // 🟢 GET - Lister tous les services
    @GetMapping
    public ResponseEntity<List<ServiceEntity>> getAllServices() {
        logger.info("Requête reçue : GET /api/services");
        List<ServiceEntity> services = serviceService.getAll();
        return ResponseEntity.ok(services);
    }

    // 🟢 GET - Récupérer un service par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        logger.info("Requête reçue : GET /api/services/{}", id);

        Optional<ServiceEntity> service = serviceService.getById(id);
        if (service.isEmpty()) {
            logger.warn("Service avec ID {} non trouvé", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucun service trouvé avec l'ID : " + id);
        }
        return ResponseEntity.ok(service.get());
    }

    // 🟢 POST - Créer un nouveau service
    @PostMapping
    public ResponseEntity<?> createService(@RequestBody ServiceEntity service) {
        logger.info("Requête reçue : POST /api/services - {}", service.getNom());

        try {
            ServiceEntity created = serviceService.create(service);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du service : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création du service : " + e.getMessage());
        }
    }

    // 🟠 PUT - Mettre à jour un service
    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable Long id, @RequestBody ServiceEntity updatedService) {
        logger.info("Requête reçue : PUT /api/services/{}", id);

        Optional<ServiceEntity> existing = serviceService.getById(id);
        if (existing.isEmpty()) {
            logger.warn("Service avec ID {} non trouvé pour mise à jour", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Service non trouvé");
        }

        ServiceEntity service = existing.get();
        service.setNom(updatedService.getNom());
        service.setDescription(updatedService.getDescription());

        ServiceEntity saved = serviceService.create(service);
        return ResponseEntity.ok(saved);
    }

    // 🔴 DELETE - Supprimer un service
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        logger.info("Requête reçue : DELETE /api/services/{}", id);

        Optional<ServiceEntity> existing = serviceService.getById(id);
        if (existing.isEmpty()) {
            logger.warn("Suppression échouée : service ID {} introuvable", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Service introuvable");
        }

        serviceService.delete(id);
        logger.info("Service ID {} supprimé avec succès", id);
        return ResponseEntity.noContent().build();
    }
}
