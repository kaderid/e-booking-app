package com.ebooking.controller;

import com.ebooking.model.Prestataire;
import com.ebooking.model.ServiceEntity;
import com.ebooking.model.User;
import com.ebooking.services.PrestataireService;
import com.ebooking.services.ServiceService;
import com.ebooking.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestataires")
@CrossOrigin(origins = "*")
public class PrestataireController {

    private static final Logger logger = LoggerFactory.getLogger(PrestataireController.class);

    private final PrestataireService prestataireService;
    private final UserService userService;
    private final ServiceService serviceService;

    public PrestataireController(PrestataireService prestataireService, UserService userService, ServiceService serviceService) {
        this.prestataireService = prestataireService;
        this.userService = userService;
        this.serviceService = serviceService;
    }

    //  GET - Lister tous les prestataires
    @GetMapping
    public ResponseEntity<List<Prestataire>> getAllPrestataires() {
        logger.info("Requête reçue : GET /api/prestataires");
        List<Prestataire> prestataires = prestataireService.getAll();
        return ResponseEntity.ok(prestataires);
    }

    //  GET - Obtenir un prestataire par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrestataireById(@PathVariable Long id) {
        Optional<Prestataire> prestataire = prestataireService.getById(id);
        if (prestataire.isEmpty()) {
            logger.warn("Prestataire ID {} non trouvé", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prestataire introuvable");
        }
        return ResponseEntity.ok(prestataire.get());
    }

    //  POST - Créer un prestataire
    @PostMapping
    public ResponseEntity<?> createPrestataire(@RequestBody Prestataire prestataire) {
        logger.info("Requête reçue : POST /api/prestataires");

        try {
            // Vérifier que le user et le service existent
            Optional<User> userOpt = userService.getUserById(prestataire.getUser().getId());
            Optional<ServiceEntity> serviceOpt = serviceService.getById(prestataire.getService().getId());

            if (userOpt.isEmpty() || serviceOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Utilisateur ou service invalide");
            }

            prestataire.setUser(userOpt.get());
            prestataire.setService(serviceOpt.get());

            Prestataire created = prestataireService.create(prestataire);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du prestataire : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création du prestataire");
        }
    }

    //  PUT - Modifier un prestataire
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrestataire(@PathVariable Long id, @RequestBody Prestataire updated) {
        logger.info("Requête reçue : PUT /api/prestataires/{}", id);

        Optional<Prestataire> existing = prestataireService.getById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prestataire non trouvé");
        }

        Prestataire prestataire = existing.get();
        prestataire.setAdresse(updated.getAdresse());
        prestataire.setSpecialite(updated.getSpecialite());

        // Mise à jour du service si fourni
        if (updated.getService() != null) {
            Optional<ServiceEntity> serviceOpt = serviceService.getById(updated.getService().getId());
            serviceOpt.ifPresent(prestataire::setService);
        }

        Prestataire saved = prestataireService.create(prestataire);
        return ResponseEntity.ok(saved);
    }

    // DELETE - Supprimer un prestataire
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrestataire(@PathVariable Long id) {
        logger.info("Requête reçue : DELETE /api/prestataires/{}", id);

        Optional<Prestataire> prestataire = prestataireService.getById(id);
        if (prestataire.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prestataire introuvable");
        }

        prestataireService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
