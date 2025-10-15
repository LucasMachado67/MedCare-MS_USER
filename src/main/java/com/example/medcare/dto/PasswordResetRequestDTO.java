package com.example.medcare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//Este fluxo é pra quando o usuário esquece a senha e precisa resetar ela
public record PasswordResetRequestDTO(
    @NotBlank(message = "O e-mail não pode ser vazio.")
    @Email(message = "Formato de e-mail inválido.")
    String email
) {
    // Implementação padrão de record
}
