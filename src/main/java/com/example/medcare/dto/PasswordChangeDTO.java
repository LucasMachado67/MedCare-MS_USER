package com.example.medcare.dto;

import jakarta.validation.constraints.NotBlank;
/**
 * DTO utilizado no processo de redefinição de senha.
 * <p>
 * Este objeto é enviado pelo usuário após clicar no link recebido por e-mail,
 * contendo o 'token' de segurança gerado pelo sistema e a nova senha escolhida.
 * </p>
 *
 * <p><b>Fluxo esperado:</b></p>
 * <ul>
 *     <li>O usuário solicita a redefinição de senha.</li>
 *     <li>O sistema envia um e-mail com um link contendo o 'token'.</li>
 *     <li>O usuário acessa esse link e envia este DTO com o 'token' e a nova senha.</li>
 * </ul>
 *
 * @param resetToken 'Token' de segurança enviado ao usuário por e-mail, obrigatório para validar o pedido de redefinição.
 * @param newPassword Nova senha escolhida pelo usuário, que deve atender aos requisitos mínimos de segurança.
 */
public record PasswordChangeDTO(
    @NotBlank(message = "O token de redefinição é obrigatório.")
    String resetToken,
    
    @NotBlank(message = "A nova senha é obrigatória.")
    String newPassword
) {
    
}
