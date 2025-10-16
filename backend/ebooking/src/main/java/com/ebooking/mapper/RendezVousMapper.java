package com.ebooking.mapper;

import com.ebooking.dto.RendezVousRequestDTO;
import com.ebooking.dto.RendezVousResponseDTO;
import com.ebooking.model.Prestataire;
import com.ebooking.model.RendezVous;
import com.ebooking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RendezVousMapper {

    @Mapping(source = "client", target = "client")
    @Mapping(source = "prestataire", target = "prestataire")
    RendezVous toEntity(RendezVousRequestDTO dto, User client, Prestataire prestataire);

    @Mapping(source = "client.prenom", target = "clientPrenom")
    @Mapping(source = "client.nom", target = "clientNom")
    @Mapping(source = "prestataire.user.prenom", target = "prestatairePrenom")
    @Mapping(source = "prestataire.user.nom", target = "prestataireNom")
    RendezVousResponseDTO toResponse(RendezVous rendezVous);
}

