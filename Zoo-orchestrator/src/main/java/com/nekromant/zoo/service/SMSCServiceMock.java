package com.nekromant.zoo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
@Slf4j
public class SMSCServiceMock implements SMSCService {

    /**
     * Отправка SMS на список номеров
     *
     * @param phones  - список телефонов
     * @param message - отправляемое сообщение
     */
    public ResponseEntity<String> sendSms(List<String> phones, String message) {
        log.info("Сообщение с текстом:'{}' было успешно отправлено на номера {}", message, String.join(",", phones));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Отправка SMS на один номер
     *
     * @param phone   - номер телефона(в формате +7...)
     * @param message - отправляемое сообщение
     * @return array (<id>, <количество sms>, <стоимость>, <баланс>) в случае успешной отправки
     * или массив (<id>, -<код ошибки>) в случае ошибки
     */
    public ResponseEntity<String> sendSms(String phone, String message) {
        log.info("Сообщение с текстом:'{}' было успешно отправлено на номер {}", message, phone);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
