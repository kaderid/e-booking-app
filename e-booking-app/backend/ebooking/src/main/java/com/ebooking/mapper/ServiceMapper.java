package com.ebooking.mapper;

import com.ebooking.dto.ServiceRequestDTO;
import com.ebooking.dto.ServiceResponseDTO;
import com.ebooking.model.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    @Mapping(target = "prestataire", ignore = true)
    ServiceEntity toEntity(ServiceRequestDTO dto);

    @Mapping(source = "prestataire.id", target = "prestataireId")
    ServiceResponseDTO toResponseDTO(ServiceEntity entity);
}
