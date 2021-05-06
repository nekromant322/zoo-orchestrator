package com.nekromant.zoo.controller.advice;

import org.springframework.http.HttpStatus;

public enum ZooError {
    //TODO Возвращать ошибки в едином формате там где это не сделано https://clck.ru/UgiYE
    ZOO_NOTIFICATION_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR),
    ZOO_NOT_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST),
    ZOO_UNEXPECTED(HttpStatus.INTERNAL_SERVER_ERROR),
    ZOO_INVALID_USER_DATA(HttpStatus.BAD_REQUEST);

    public HttpStatus httpStatus;

    ZooError(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
