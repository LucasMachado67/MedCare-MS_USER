package com.example.medcare.dto;

import java.util.List;


public record LoginResponseDTO(String token,
                               String username,
                               long personId,
                               List<String> roles) {
} 
