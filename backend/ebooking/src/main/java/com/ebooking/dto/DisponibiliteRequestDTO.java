package com.ebooking.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class DisponibiliteRequestDTO {
    private String jourSemaine;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Long prestataireId;
}
