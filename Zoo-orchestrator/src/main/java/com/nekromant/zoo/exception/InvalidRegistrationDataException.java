package com.nekromant.zoo.exception;

public class InvalidRegistrationDataException extends RuntimeException{
    public InvalidRegistrationDataException (String message) {
        super(message);
    }
}
