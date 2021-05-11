package com.nekromant.zoo.controller.advice;

import org.springframework.http.HttpStatus;

public enum ZooError {
    ZOO_NOTIFICATION_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR),
    ZOO_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST),
    ZOO_UNEXPECTED(HttpStatus.INTERNAL_SERVER_ERROR),
    ZOO_INVALID_USER_DATA(HttpStatus.BAD_REQUEST),
    ZOO_CHANGE_PASSWORD_FAILED(HttpStatus.BAD_REQUEST),
    ZOO_AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED),
    ZOO_AUTHORITIES_NOT_FOUND(HttpStatus.BAD_REQUEST);

    public HttpStatus httpStatus;

    ZooError(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
