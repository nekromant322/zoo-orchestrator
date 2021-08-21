package com.nekromant.zoo.client;

import dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationZooClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.url.notificationZoo}")
    private String url;

    public void sendEmail(NotificationDTO notificationDTO) {
        restTemplate.postForEntity(url + "/api/notify/sendEmail", notificationDTO, NotificationDTO.class);
    }

    public void sendSms(NotificationDTO notificationDTO) {
        restTemplate.postForEntity(url + "/api/notify/sendSms", notificationDTO, NotificationDTO.class);
    }
}
