package com.ebooking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "rendezvous")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lien vers le client
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    // Lien vers le prestataire
    @ManyToOne
    @JoinColumn(name = "prestataire_id", nullable = false)
    private Prestataire prestataire;

    private LocalDate date;
    private LocalTime heure;

    @Enumerated(EnumType.STRING)
    private StatutRendezVous statut = StatutRendezVous.EN_ATTENTE;; // Valeur par défaut
}
