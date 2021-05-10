package com.nekromant.zoo.exception;

public class SmsSendFailedException extends RuntimeException{
    public SmsSendFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
