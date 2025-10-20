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

    private Long clientId;
    private String clientPrenom;
    private String clientNom;

    private Long prestataireId;
    private String prestatairePrenom;
    private String prestataireNom;
    private Long serviceId;
    private String serviceNom;

    private LocalDate date;
    private LocalTime heure;
    private String statut;
}
