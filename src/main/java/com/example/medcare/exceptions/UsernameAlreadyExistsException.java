package com.example.medcare.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando ocorre uma tentativa de registrar ou criar um usuário
 * com um nome de usuário (username) que já está em uso no sistema.
 *
 * <p>
 * Esta exceção é anotada com {@link ResponseStatus}, o que faz com que o
 * Spring retorne automaticamente o status HTTP {@code 409 CONFLICT}
 * quando ela for lançada em uma requisição REST.
 * </p>
 *
 * <p>
 * A mensagem gerada informa claramente qual nome de usuário está duplicado,
 * facilitando o entendimento tanto para o desenvolvedor quanto para o cliente da API.
 * </p>
 *
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyExistsException extends RuntimeException {

    /**
     * Construtor que cria a exceção informando o nome de usuário em conflito.
     *
     * @param username o nome de usuário que já está registrado no sistema.
     */
    public UsernameAlreadyExistsException(String username) {
        super("O nome de usuário '" + username + "' já está em uso. Escolha outro.");
    }
}
