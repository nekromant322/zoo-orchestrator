package com.nekromant.zoo.service;
/*
 * SMSC.RU API (smsc.ru) версия 1.3 (03.07.2019) smsc's sms sender package
 */


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SMSCService {

    @Value("${SMSC.login}")
    private String SMSC_LOGIN;

    @Value("${SMSC.password}")
    private String SMSC_PASSWORD;

    /**
     * Отправка SMS
     *
     * @param phoneNumber   - список телефонов через запятую или точку с запятой
     * @param message  - отправляемое сообщение
     */

    public ResponseEntity<String> send(String phoneNumber,String message){
        RestTemplate restTemplate = new RestTemplate();
        Map<String,String> params = new HashMap<>();
        params.put("login",SMSC_LOGIN);
        params.put("password",SMSC_PASSWORD);
        params.put("phones",phoneNumber);
        params.put("message",message);
        ResponseEntity<String> response
                = restTemplate.getForEntity("https://smsc.ru/sys/send.php?login={login}&psw={password}&phones={phones}&mes={message}", String.class,params);
    return response;
    }


}