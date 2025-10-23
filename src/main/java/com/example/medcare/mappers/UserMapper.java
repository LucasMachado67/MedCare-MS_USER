package com.example.medcare.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.ComponentScan;

import com.example.medcare.dto.RegisterRequestDTO;
import com.example.medcare.dto.UserResponseDto;
import com.example.medcare.models.User;

@ComponentScan
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converte a Entidade User para o DTO de Resposta (GET /me).
     * O MapStruct automaticamente ignora o campo 'password' que não está no DTO.
     */
    UserResponseDto toUserResponseDTO(User user);
    

    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUser(RegisterRequestDTO dto);
}
