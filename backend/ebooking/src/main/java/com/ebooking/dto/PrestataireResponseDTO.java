package com.ebooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrestataireResponseDTO {

    private Long id;
    private String nomComplet;   // concaténation prenom + nom du user
    private String email;
    private String telephone;
    private String specialite;
    private String adresse;
    private String serviceNom;
}
