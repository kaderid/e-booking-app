package com.ebooking.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = " Prestataire")
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
    @JoinColumn(name = "service_id")
    private ServiceEntity categorieService;  // Catégorie principale du prestataire

    @OneToMany(mappedBy = "prestataire", cascade = CascadeType.ALL)
    private List<ServiceEntity> services = new ArrayList<>();  // Services proposés par le prestataire
}

