package com.ebooking.services;

import com.ebooking.dto.StatistiquesDTO;
import com.ebooking.dto.StatistiquesPrestataireDTO;
import com.ebooking.dto.UserResponseDTO;
import com.ebooking.mapper.UserMapper;
import com.ebooking.model.*;
import com.ebooking.repository.PrestataireRepository;
import com.ebooking.repository.RendezVousRepository;
import com.ebooking.repository.ServiceRepository;
import com.ebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RendezVousRepository rendezVousRepository;
    private final PrestataireRepository prestataireRepository;
    private final ServiceRepository serviceRepository;
    private final UserMapper userMapper;

    public UserResponseDTO activerCompte(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        user.setStatut(Statut.ACTIF);
        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    public UserResponseDTO bloquerCompte(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        user.setStatut(Statut.BLOQUE);
        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    public StatistiquesDTO getStatistiques() {
        long nombreTotalRendezVous = rendezVousRepository.count();
        long nombreRendezVousEnAttente = rendezVousRepository.findAll().stream()
                .filter(rdv -> rdv.getStatut() == StatutRendezVous.EN_ATTENTE)
                .count();
        long nombreRendezVousConfirmes = rendezVousRepository.findAll().stream()
                .filter(rdv -> rdv.getStatut() == StatutRendezVous.CONFIRME)
                .count();
        long nombreRendezVousAnnules = rendezVousRepository.findAll().stream()
                .filter(rdv -> rdv.getStatut() == StatutRendezVous.ANNULE)
                .count();
        long nombreTotalClients = userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.CLIENT)
                .count();
        long nombreTotalPrestataires = prestataireRepository.count();
        long nombreTotalServices = serviceRepository.count();

        return new StatistiquesDTO(
                nombreTotalRendezVous,
                nombreRendezVousEnAttente,
                nombreRendezVousConfirmes,
                nombreRendezVousAnnules,
                nombreTotalClients,
                nombreTotalPrestataires,
                nombreTotalServices
        );
    }

    public List<StatistiquesPrestataireDTO> getStatistiquesPrestataires() {
        List<Prestataire> prestataires = prestataireRepository.findAll();
        List<StatistiquesPrestataireDTO> statistiques = new ArrayList<>();

        for (Prestataire prestataire : prestataires) {
            User user = prestataire.getUser();
            ServiceEntity service = prestataire.getCategorieService();

            // Récupérer tous les rendez-vous du prestataire
            List<RendezVous> rendezVousPrestataire = rendezVousRepository.findAll().stream()
                    .filter(rdv -> rdv.getPrestataire().getId().equals(prestataire.getId()))
                    .collect(Collectors.toList());

            // Calculer les statistiques
            long nombreRendezVous = rendezVousPrestataire.size();
            long nombreConfirmes = rendezVousPrestataire.stream()
                    .filter(rdv -> rdv.getStatut() == StatutRendezVous.CONFIRME)
                    .count();
            long nombreEnAttente = rendezVousPrestataire.stream()
                    .filter(rdv -> rdv.getStatut() == StatutRendezVous.EN_ATTENTE)
                    .count();
            long nombreAnnules = rendezVousPrestataire.stream()
                    .filter(rdv -> rdv.getStatut() == StatutRendezVous.ANNULE)
                    .count();

            // Compter les clients uniques
            Set<Long> clientIds = new HashSet<>();
            for (RendezVous rdv : rendezVousPrestataire) {
                clientIds.add(rdv.getClient().getId());
            }
            long nombreClientsUniques = clientIds.size();

            StatistiquesPrestataireDTO stat = StatistiquesPrestataireDTO.builder()
                    .prestataireId(prestataire.getId())
                    .prestatairePrenom(user.getPrenom())
                    .prestataireNom(user.getNom())
                    .prestataireEmail(user.getEmail())
                    .serviceName(service != null ? service.getNom() : "Non défini")
                    .nombreRendezVous(nombreRendezVous)
                    .nombreRendezVousConfirmes(nombreConfirmes)
                    .nombreRendezVousEnAttente(nombreEnAttente)
                    .nombreRendezVousAnnules(nombreAnnules)
                    .nombreClientsUniques(nombreClientsUniques)
                    .build();

            statistiques.add(stat);
        }

        return statistiques;
    }
}
