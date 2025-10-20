package com.ebooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String telephone;
    private String role;
    private String statut;
}
