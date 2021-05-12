package com.nekromant.zoo.service;
/*
 * SMSC.RU API (smsc.ru) версия 1.3 (03.07.2019) smsc's sms sender package
 */


import com.nekromant.zoo.exception.SmsSendFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SMSCService {

    @Value("${SMSC.login}")
    private String SMSC_LOGIN;

    @Value("${SMSC.password}")
    private String SMSC_PASSWORD;

    @Value("${SMSC.url}")
    private String SMSC_URL;

    private final static String LOGIN_KEY = "login";
    private final static String PASSWORD_KEY = "password";
    private final static String PHONES_KEY = "phones";
    private final static String MESSAGE_KEY = "message";
    private final static String PARAMS = "?login={login}&psw={password}&phones={phones}&mes={message}";

    /**
     * Отправка SMS на список номеров
     *
     * @param phones  - список телефонов
     * @param message - отправляемое сообщение
     */
    public ResponseEntity<String> sendSms(List<String> phones, String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> params = new HashMap<>();
            params.put(LOGIN_KEY, SMSC_LOGIN);
            params.put(PASSWORD_KEY, SMSC_PASSWORD);
            String joinedPhones = String.join(";", phones);
            params.put(PHONES_KEY, joinedPhones);
            params.put(MESSAGE_KEY, message);
            return restTemplate.getForEntity(SMSC_URL, String.class, params);
        } catch (Throwable exception) {
            throw new SmsSendFailedException(
                    "Не удалось отправить смс на номера " + phones.stream().collect(Collectors.joining(",", "{", "}")),
                    exception);
        }
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
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> params = new HashMap<>();
            params.put(LOGIN_KEY, SMSC_LOGIN);
            params.put(PASSWORD_KEY, SMSC_PASSWORD);
            params.put(PHONES_KEY, phone);
            params.put(MESSAGE_KEY, message);
            return restTemplate.getForEntity(SMSC_URL + PARAMS, String.class, params);
        } catch (Throwable exception) {
            throw new SmsSendFailedException("Не удалось отправить смс на номер " + phone, exception);
        }
    }
}