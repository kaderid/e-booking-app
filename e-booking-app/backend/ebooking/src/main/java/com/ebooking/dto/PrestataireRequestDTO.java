package com.ebooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrestataireRequestDTO {

    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long userId;  // le User associé

    @NotBlank(message = "La spécialité est obligatoire")
    private String specialite;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotNull(message = "L'identifiant du service est obligatoire")
    private Long serviceId;
}
