package com.ebooking.mapper;

import com.ebooking.dto.UserRequestDTO;
import com.ebooking.dto.UserResponseDTO;
import com.ebooking.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO dto);
    UserResponseDTO toResponse(User user);
}