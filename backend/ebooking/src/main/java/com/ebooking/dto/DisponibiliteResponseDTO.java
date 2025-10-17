package com.ebooking.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalTime;

@Data
@Builder
public class DisponibiliteResponseDTO {
    private Long id;
    private String jourSemaine;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Long prestataireId;
}