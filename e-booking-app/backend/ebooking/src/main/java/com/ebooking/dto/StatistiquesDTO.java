package com.ebooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesDTO {
    private long nombreTotalRendezVous;
    private long nombreRendezVousEnAttente;
    private long nombreRendezVousConfirmes;
    private long nombreRendezVousAnnules;
    private long nombreTotalClients;
    private long nombreTotalPrestataires;
    private long nombreTotalServices;
}
