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

    // 🔹 Conversion DTO → Entité
    @Mapping(target = "id", ignore = true) // on ignore l'id car il est auto-généré
    @Mapping(target = "services", ignore = true) // Liste des services propres au prestataire
    @Mapping(source = "user", target = "user")
    @Mapping(source = "service", target = "categorieService")
    Prestataire toEntity(PrestataireRequestDTO dto, User user, ServiceEntity service);

    // 🔹 Conversion Entité → DTO de réponse
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.prenom", target = "prenom")
    @Mapping(source = "user.nom", target = "nom")
    @Mapping(source = "categorieService.nom", target = "serviceNom")
    PrestataireResponseDTO toResponse(Prestataire prestataire);
}
