package com.example.medcare.dto;

import com.example.medcare.enums.UserRole;

//Talvez futuramente enviar o userRole junto;
public record LoginResponseDTO(String token,
                               String username,
                               long personId,
                               UserRole role) {
} 
