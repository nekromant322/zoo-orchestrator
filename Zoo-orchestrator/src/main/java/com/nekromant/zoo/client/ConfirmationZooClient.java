package com.nekromant.zoo.client;

import dto.ConfirmationTokenDTO;
import dto.SMSCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConfirmationZooClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.url.confirmationZoo}")
    private String url;

    public ConfirmationTokenDTO getToken(String token) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        return restTemplate.getForEntity(url + "/api/token", ConfirmationTokenDTO.class, params).getBody();
    }

    public String getEncodedToken(String email, String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("phone", phone);
        return restTemplate.getForObject(url + "/api/token/encoded", String.class, params);
    }

    public void createToken(String token, String email) {
        ConfirmationTokenDTO tokenDTO = new ConfirmationTokenDTO(0, token, email, null);
        restTemplate.postForEntity(url + "/api/token/create", tokenDTO, ConfirmationTokenDTO.class);
    }

    public void removeToken(ConfirmationTokenDTO confirmationTokenDTO) {
        restTemplate.postForEntity(url + "/api/token/delete", confirmationTokenDTO, ConfirmationTokenDTO.class);
    }

    public Long createSMSCode(SMSCodeDTO smsCodeDTO) {
        return restTemplate.postForEntity(url + "/api/smsCode/create", smsCodeDTO, Long.class).getBody();
    }

    public void verifySMSCode(SMSCodeDTO smsCodeDTO) {
        restTemplate.postForEntity(url + "/api/smsCode/verify", smsCodeDTO, SMSCodeDTO.class);
    }
}
