package com.ebooking.mapper;

import com.ebooking.dto.DisponibiliteRequestDTO;
import com.ebooking.dto.DisponibiliteResponseDTO;
import com.ebooking.model.Disponibilite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DisponibiliteMapper {

    // Entité -> DTO
    @Mapping(source = "prestataire.id", target = "prestataireId")
    DisponibiliteResponseDTO toDto(Disponibilite disponibilite);

    // DTO -> Entité
    @Mapping(source = "prestataireId", target = "prestataire.id")
    Disponibilite toEntity(DisponibiliteRequestDTO dto);
}
