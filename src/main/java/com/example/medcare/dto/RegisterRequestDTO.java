package com.example.medcare.dto;

import com.example.medcare.enums.UserRole;

import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO(@NotNull String username,@NotNull String password,UserRole role, long personId ) {
    
}
