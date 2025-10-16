package com.ebooking.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVousResponseDTO {
    private Long id;
    private String clientNomComplet;
    private String prestataireNomComplet;
    private String serviceNom;
    private LocalDate date;
    private LocalTime heure;
    private String statut;
}
