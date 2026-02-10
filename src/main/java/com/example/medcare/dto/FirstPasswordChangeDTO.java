package com.example.medcare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para troca de senha obrigatória no primeiro acesso.
 */
public record FirstPasswordChangeDTO(
        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String newPassword
) {
}
