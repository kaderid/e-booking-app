package com.ebooking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestDTO {
    private String nom;
    private String description;
    private Integer duree;  // Durée en minutes
    private Double prix;
    private Long prestataireId;
}
