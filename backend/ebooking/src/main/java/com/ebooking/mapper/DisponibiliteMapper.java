package com.ebooking.mapper;

import com.ebooking.dto.DisponibiliteRequestDTO;
import com.ebooking.dto.DisponibiliteResponseDTO;
import com.ebooking.model.Disponibilite;
import com.ebooking.model.Prestataire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DisponibiliteMapper {

    @Mapping(source = "prestataire", target = "prestataire")
    Disponibilite toEntity(DisponibiliteRequestDTO dto, Prestataire prestataire);

    @Mapping(source = "prestataire.user.prenom", target = "prestatairePrenom")
    @Mapping(source = "prestataire.user.nom", target = "prestataireNom")
    DisponibiliteResponseDTO toResponse(Disponibilite disponibilite);
}