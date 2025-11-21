package com.example.medcare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para iniciar o processo de redefinição de senha.
 *
 * <p>
 * Este objeto deve ser enviado pelo usuário quando ele solicita a recuperação
 * da senha esquecida. O sistema utilizará o e-mail fornecido para verificar
 * se existe um usuário associado e, caso positivo, enviará um link contendo
 * um token seguro para redefinição de senha.
 * </p>
 *
 * <p><b>Fluxo esperado:</b></p>
 * <ul>
 *     <li>O usuário informa o e-mail cadastrado.</li>
 *     <li>O sistema valida o e-mail e gera um token de redefinição.</li>
 *     <li>Um e-mail com instruções é enviado ao usuário.</li>
 * </ul>
 *
 * @param email E-mail cadastrado no sistema, obrigatório e em formato válido.
 */
public record PasswordResetRequestDTO(
    @NotBlank(message = "O e-mail não pode ser vazio.")
    @Email(message = "Formato de e-mail inválido.")
    String email
) {

}
