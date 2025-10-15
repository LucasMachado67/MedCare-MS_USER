package com.example.medcare.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.medcare.dto.RegisterRequestDTO;
import com.example.medcare.dto.UserResponseDto;
import com.example.medcare.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converte a Entidade User para o DTO de Resposta (GET /me).
     * O MapStruct automaticamente ignora o campo 'password' que não está no DTO.
     */
    UserResponseDto toUserResponseDTO(User user);

    @Mapping(target = "authorities", ignore = true)
    User toUser(RegisterRequestDTO dto);
}
