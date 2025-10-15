package com.example.medcare.exceptions;

public class InvalidCpfException extends RuntimeException{

    public InvalidCpfException(String cpf){
        super(cpf);
    }
}
