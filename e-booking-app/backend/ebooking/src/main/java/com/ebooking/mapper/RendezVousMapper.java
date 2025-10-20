package com.ebooking.mapper;

import com.ebooking.dto.RendezVousRequestDTO;
import com.ebooking.dto.RendezVousResponseDTO;
import com.ebooking.model.RendezVous;
import com.ebooking.model.Prestataire;
import com.ebooking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RendezVousMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "client", target = "client")
    @Mapping(source = "prestataire", target = "prestataire")
    @Mapping(target = "statut", expression = "java(com.ebooking.model.StatutRendezVous.EN_ATTENTE)") // statut par défaut
    RendezVous toEntity(RendezVousRequestDTO dto, User client, Prestataire prestataire);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.prenom", target = "clientPrenom")
    @Mapping(source = "client.nom", target = "clientNom")
    @Mapping(source = "prestataire.id", target = "prestataireId")
    @Mapping(source = "prestataire.user.prenom", target = "prestatairePrenom")
    @Mapping(source = "prestataire.user.nom", target = "prestataireNom")
    @Mapping(source = "prestataire.categorieService.id", target = "serviceId")
    @Mapping(source = "prestataire.categorieService.nom", target = "serviceNom")
    @Mapping(source = "statut", target = "statut")
    RendezVousResponseDTO toResponse(RendezVous rendezVous);
}
