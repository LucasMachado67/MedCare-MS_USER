package com.example.medcare.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyExistsException extends RuntimeException{

    // Construtor que aceita o username para a mensagem de erro
    public UsernameAlreadyExistsException(String username) {
        super("O nome de usuário '" + username + "' já está em uso. Escolha outro.");
    }
}
