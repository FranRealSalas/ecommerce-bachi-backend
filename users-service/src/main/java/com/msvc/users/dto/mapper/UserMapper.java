package com.msvc.users.dto.mapper;

import com.msvc.users.dto.request.UserCreationRequestDTO;
import com.msvc.users.dto.request.UserEditRequestDTO;
import com.msvc.users.dto.response.UserResponseDTO;
import com.msvc.users.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    public abstract User toEntity(UserCreationRequestDTO userDTO);

    public abstract User toEntity(UserEditRequestDTO userDTO, @MappingTarget User user);

    public abstract UserResponseDTO toDto(User user);

}
