package com.example.medcare.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.medcare.dto.AddressDTO;
import com.example.medcare.dto.AddressResponseDTO;
import com.example.medcare.models.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true) 
    Address toEntityCreation(AddressDTO dto);
    AddressDTO toDtoCreation(Address entity);

    Address toEntityResponse(AddressResponseDTO dto);
    @Mapping(target = "id", ignore = true)
    AddressResponseDTO toDtoResponse(Address entity);
    
    List<Address> toEntityResponse(List<AddressResponseDTO> dtos);
    List<AddressResponseDTO> toDtoResponse(List<Address> entities);

    @Mapping(target = "id", ignore = true)
    void updateAddressFromDto(AddressDTO dto, @MappingTarget Address target);
}

