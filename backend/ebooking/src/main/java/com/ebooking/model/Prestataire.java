package com.ebooking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prestataires")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestataire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specialite;
    private String adresse;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;
}

