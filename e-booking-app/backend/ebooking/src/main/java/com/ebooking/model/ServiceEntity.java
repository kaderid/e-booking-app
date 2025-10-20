package com.ebooking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private Integer duree; // Durée en minutes
    private Double prix;

    @ManyToOne
    @JoinColumn(name = "prestataire_id")
    private Prestataire prestataire;
}

