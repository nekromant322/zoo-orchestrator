package com.nekromant.zoo.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException (String message) {
        super(message);
    }
}
