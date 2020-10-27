package com.nekromant.zoo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VkPhotoSchedule {

    private static final int time = 86400000;

    @Autowired
    private VkPhotoService vkPhotoService;

    @Scheduled(fixedRate = time)
    private void scheduleVkApiPhotoGetter(){
        vkPhotoService.getPhotoUrl();
    }
}
