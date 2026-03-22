package com.example.medcare.dto;

import com.example.medcare.enums.UserRole;

/**
 * DTO retornado após um processo de autenticação bem-sucedido.
 * <p>
 * Este objeto contém as informações essenciais do usuário autenticado,
 * incluindo o 'token' JWT, dados de identificação e o papel (role) associado.
 * </p>
 *
 * @param token     'Token' JWT gerado após o 'login', utilizado para autenticação nas próximas requisições.
 * @param personId  'ID' da pessoa associada ao usuário autenticado.
 * @param role      Papel (UserRole) do usuário no sistema, definindo permissões de acesso.
 */
public record LoginResponseDTO(String token,
                               String email,
                               long personId,
                               UserRole role,
                               String tenantId,
                               boolean mustChangePassword) {
} 
