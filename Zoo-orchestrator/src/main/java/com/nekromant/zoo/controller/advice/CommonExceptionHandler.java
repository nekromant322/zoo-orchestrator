package com.nekromant.zoo.controller.advice;

import com.nekromant.zoo.exception.*;
import dto.CommonErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;

import static com.nekromant.zoo.controller.advice.ZooError.*;


/**
 * Единый интерфейс API обработки ошибок
 */
@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<CommonErrorDTO> handleException(Exception e, ServletWebRequest request) {
        log.error(e.getMessage(), e);
        ZooError error = getError(e);
        return ResponseEntity.status(error.httpStatus).body(createDto(error, e));
    }

    /**
     * Маппинг исключения на ошибку
     */
    private ZooError getError(Exception exception) {
        if (exception instanceof EmailSendFailedException) {
            return ZOO_NOTIFICATION_SEND_FAILED;
        }
        if (exception instanceof SmsSendFailedException) {
            return ZOO_NOTIFICATION_SEND_FAILED;
        }
        if (exception instanceof AnimalRequestNotFoundException) {
            return ZOO_REQUEST_NOT_FOUND;
        }
        if (exception instanceof UserAlreadyExistException) {
            return ZOO_INVALID_USER_DATA;
        }
        if (exception instanceof InvalidRegistrationDataException) {
            return ZOO_INVALID_USER_DATA;
        }
        if (exception instanceof InvalidChangePasswordDataException) {
            return ZOO_CHANGE_PASSWORD_FAILED;
        }
        if (exception instanceof InvalidLoginException) {
            return ZOO_AUTHORIZATION_FAILED;
        }
        if (exception instanceof AuthoritiesNotFoundException) {
            return ZOO_AUTHORITIES_NOT_FOUND;
        }
        return ZOO_UNEXPECTED;
    }

    /**
     * Создание дто ошибки
     */
    private CommonErrorDTO createDto(ZooError error, Exception ex) {
        return CommonErrorDTO
                .builder()
                .code(error.name())
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();
    }

}
