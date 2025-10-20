package com.ebooking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatistiquesPrestataireDTO {
    private Long prestataireId;
    private String prestatairePrenom;
    private String prestataireNom;
    private String prestataireEmail;
    private String serviceName;
    private Long nombreRendezVous;
    private Long nombreRendezVousConfirmes;
    private Long nombreRendezVousEnAttente;
    private Long nombreRendezVousAnnules;
    private Long nombreClientsUniques;
}
