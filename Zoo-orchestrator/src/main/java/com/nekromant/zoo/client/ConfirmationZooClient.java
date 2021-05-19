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

    private final String tokenUrl = url + "/api/token";
    private final String encodedTokenPath = tokenUrl + "/encoded";
    private final String createTokenPath = tokenUrl + "/create";
    private final String removeTokenPath = tokenUrl + "/delete";

    private final String smsCodeUrl = url + "/api/smsCode";
    private final String createSmsCodePath = smsCodeUrl + "/create";
    private final String verifySmsCodePath = smsCodeUrl + "/verify";

    public ConfirmationTokenDTO getToken(String token) {
        Map<String, String> params = new HashMap<>();
        params.put("token", token);
        return restTemplate.getForEntity(tokenUrl, ConfirmationTokenDTO.class, params).getBody();
    }

    public String getEncodedToken(String email, String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("phone", phone);
        return restTemplate.getForObject(encodedTokenPath, String.class, params);
    }

    public void createToken(String token, String email) {
        ConfirmationTokenDTO tokenDTO = new ConfirmationTokenDTO(0, token, email, null);
        restTemplate.postForEntity(createTokenPath, tokenDTO, ConfirmationTokenDTO.class);
    }

    public void removeToken(ConfirmationTokenDTO confirmationTokenDTO) {
        restTemplate.postForEntity(removeTokenPath, confirmationTokenDTO, ConfirmationTokenDTO.class);
    }

    public void createSMSCode(SMSCodeDTO smsCodeDTO) {
        restTemplate.postForEntity(createSmsCodePath, smsCodeDTO, SMSCodeDTO.class);
    }

    public void verifySMSCode(SMSCodeDTO smsCodeDTO) {
        restTemplate.postForEntity(verifySmsCodePath, smsCodeDTO, SMSCodeDTO.class);
    }
}
