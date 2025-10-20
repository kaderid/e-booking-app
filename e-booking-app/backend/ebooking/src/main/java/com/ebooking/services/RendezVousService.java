package com.ebooking.services;

import com.ebooking.dto.RendezVousRequestDTO;
import com.ebooking.dto.RendezVousResponseDTO;
import com.ebooking.mapper.RendezVousMapper;
import com.ebooking.model.Disponibilite;
import com.ebooking.model.Prestataire;
import com.ebooking.model.RendezVous;
import com.ebooking.model.User;
import com.ebooking.repository.DisponibiliteRepository;
import com.ebooking.repository.PrestataireRepository;
import com.ebooking.repository.RendezVousRepository;
import com.ebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final RendezVousMapper rendezVousMapper;
    private final UserRepository userRepository;
    private final PrestataireRepository prestataireRepository;
    private final DisponibiliteRepository disponibiliteRepository;

    public RendezVousResponseDTO createRendezVous(RendezVousRequestDTO dto) {
        User client = userRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));
        Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        // Vérifier que le rendez-vous n'est pas dans le passé
        if (dto.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Impossible de prendre un rendez-vous dans le passé");
        }

        // Vérifier que le client n'a pas déjà un rendez-vous au même créneau horaire le même jour
        boolean clientHasRendezVous = rendezVousRepository.findClientRendezVousAtSameTime(
                client.getId(), dto.getDate(), dto.getHeure()
        ).isPresent();

        if (clientHasRendezVous) {
            throw new IllegalArgumentException("Vous avez déjà un rendez-vous prévu à cette date et heure");
        }

        // Vérifier les disponibilités du prestataire
        if (!isWithinDisponibilites(prestataire.getId(), dto.getDate(), dto.getHeure())) {
            throw new IllegalArgumentException("Le prestataire n'est pas disponible à cette date et heure");
        }

        // Vérifier qu'il n'y a pas déjà un rendez-vous à cette heure pour ce prestataire
        boolean prestataireHasRendezVous = rendezVousRepository.findExistingRendezVous(
                prestataire.getId(), dto.getDate(), dto.getHeure()
        ).isPresent();

        if (prestataireHasRendezVous) {
            throw new IllegalArgumentException("Le prestataire a déjà un rendez-vous à cette heure");
        }

        RendezVous entity = rendezVousMapper.toEntity(dto, client, prestataire);
        RendezVous saved = rendezVousRepository.save(entity);
        return rendezVousMapper.toResponse(saved);
    }

    private boolean isWithinDisponibilites(Long prestataireId, LocalDate date, LocalTime heure) {
        // Obtenir le jour de la semaine en français
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String jourFrancais = capitalizeFirstLetter(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.FRENCH));

        // Récupérer les disponibilités du prestataire pour ce jour
        List<Disponibilite> disponibilites = disponibiliteRepository.findByPrestataireId(prestataireId)
                .stream()
                .filter(d -> d.getJourSemaine().equalsIgnoreCase(jourFrancais))
                .collect(Collectors.toList());

        if (disponibilites.isEmpty()) {
            return false;
        }

        // Vérifier si l'heure est dans l'une des plages de disponibilité
        return disponibilites.stream()
                .anyMatch(d -> !heure.isBefore(d.getHeureDebut()) && heure.isBefore(d.getHeureFin()));
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public List<RendezVousResponseDTO> getAllRendezVous() {
        return rendezVousRepository.findAll()
                .stream()
                .map(rendezVousMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<RendezVousResponseDTO> getRendezVousByClientId(Long clientId) {
        return rendezVousRepository.findByClientId(clientId)
                .stream()
                .map(rendezVousMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<RendezVousResponseDTO> getRendezVousByPrestataireId(Long prestataireId) {
        return rendezVousRepository.findByPrestataireId(prestataireId)
                .stream()
                .map(rendezVousMapper::toResponse)
                .collect(Collectors.toList());
    }

    public RendezVousResponseDTO updateRendezVous(Long id, RendezVousRequestDTO dto) {
        RendezVous rendezVous = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable"));

        if (dto.getDate() != null) {
            rendezVous.setDate(dto.getDate());
        }
        if (dto.getHeure() != null) {
            rendezVous.setHeure(dto.getHeure());
        }
        if (dto.getStatut() != null) {
            rendezVous.setStatut(dto.getStatut());
        }

        RendezVous updated = rendezVousRepository.save(rendezVous);
        return rendezVousMapper.toResponse(updated);
    }

    public void deleteRendezVous(Long id) {
        if (!rendezVousRepository.existsById(id)) {
            throw new RuntimeException("Rendez-vous introuvable");
        }
        rendezVousRepository.deleteById(id);
    }
}
