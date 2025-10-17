package com.ebooking.mapper;

import com.ebooking.dto.ServiceRequestDTO;
import com.ebooking.dto.ServiceResponseDTO;
import com.ebooking.model.ServiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceEntity toEntity(ServiceRequestDTO dto);
    ServiceResponseDTO toResponseDTO(ServiceEntity entity);
}
