package com.ebooking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponseDTO {
    private Long id;
    private String nom;
    private String description;
    private Integer duree;
    private Double prix;
    private Long prestataireId;
}
