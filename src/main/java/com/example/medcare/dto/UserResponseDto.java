package com.example.medcare.dto;

import java.util.List;
import java.util.UUID;

import com.example.medcare.enums.UserRole;

public record UserResponseDto(UUID id, String username, List<UserRole> roles, long personId) {

}
