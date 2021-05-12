package com.nekromant.zoo.exception;

public class AuthoritiesNotFoundException extends RuntimeException{
    public AuthoritiesNotFoundException(String message) {
        super(message);
    }
}
