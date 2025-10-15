package com.example.medcare.dto;

import java.util.List;

import com.example.medcare.enums.UserRole;

import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO(@NotNull String username,@NotNull String password,List<UserRole> roles, long personId ) {
    
}
