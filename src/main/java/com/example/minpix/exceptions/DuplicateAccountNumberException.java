package com.example.minpix.exceptions;

public class DuplicateAccountNumberException extends RuntimeException {

    public DuplicateAccountNumberException(String numeroConta) {
        super("Já existe uma conta com o número " + numeroConta + ".");
    }
}
