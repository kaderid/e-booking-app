package com.ebooking.mapper;

import com.ebooking.dto.PrestataireRequestDTO;
import com.ebooking.dto.PrestataireResponseDTO;
import com.ebooking.model.Prestataire;
import com.ebooking.model.ServiceEntity;
import com.ebooking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrestataireMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "service", target = "service")
    Prestataire toEntity(PrestataireRequestDTO dto, User user, ServiceEntity service);

    @Mapping(source = "user.prenom", target = "prenom")
    @Mapping(source = "user.nom", target = "nom")
    @Mapping(source = "service.nom", target = "serviceNom")
    PrestataireResponseDTO toResponse(Prestataire prestataire);
}
