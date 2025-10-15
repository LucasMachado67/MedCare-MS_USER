package com.example.medcare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
//Este DTO é usado quando o usuário recebe o e-mail, clica no link e precisa enviar a nova senha junto com o token de segurança.
public record PasswordChangeDTO(
    @NotBlank(message = "O token de redefinição é obrigatório.")
    String resetToken,
    
    @NotBlank(message = "A nova senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
    String newPassword
) {
    
}
