package com.example.medcare.dto;

/**
 * DTO utilizado para transportar os dados de autenticação de um usuário.
 * <p>
 * Este objeto é enviado no corpo da requisição na operação de login,
 * contendo o username e a senha informados pelo usuário.
 * </p>
 *
 * @param email Email de usuário utilizado para autenticação.
 * @param password Senha correspondente ao usuário.
 */
public record AuthenticationDTO(String email, String password) {
    
}