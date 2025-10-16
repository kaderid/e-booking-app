package com.ebooking.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVousRequestDTO {
    private Long clientId;
    private Long prestataireId;
    private LocalDate date;
    private LocalTime heure;
}
