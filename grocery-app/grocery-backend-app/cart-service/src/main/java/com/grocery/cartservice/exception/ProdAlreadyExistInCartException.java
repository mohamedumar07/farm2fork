package com.grocery.cartservice.exception;

public class ProdAlreadyExistInCartException extends RuntimeException {
    public ProdAlreadyExistInCartException(String message) {
        super(message);
    }
}
