package com.example.medcare.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneAlreadyExistsException extends RuntimeException{

    public PhoneAlreadyExistsException() {
        super("O telefone informado já está em uso");
    }
}
