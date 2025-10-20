package com.ebooking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestataireResponseDTO {
    private Long id;
    private String prenom;
    private String nom;
    private String specialite;
    private String adresse;
    private String description;
    private String serviceNom;
}
