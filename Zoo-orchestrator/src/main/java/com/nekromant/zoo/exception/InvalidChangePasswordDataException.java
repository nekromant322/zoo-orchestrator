package com.nekromant.zoo.exception;

public class InvalidChangePasswordDataException extends RuntimeException{
    public InvalidChangePasswordDataException (String message) {
        super(message);
    }
}
