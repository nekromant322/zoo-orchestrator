package com.nekromant.zoo.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
@Slf4j
public class VkPhotoService {

    @Value("${vk.app.id}")
    private String APP_ID;

    @Value("${vk.client.id}")
    private String CLIENT_ID;

    @Value("${vk.service.token}")
    private String SERVICE_TOKEN;

    @Value("${vk.album.id}")
    private String ALBUM_ID;

    @Value("${vk.owner.id}")
    private int OWNER_ID;

    @Autowired
    private PhotoCacheService photoCacheService;

    @Autowired
    private VkApiClient vk;

    /**
     * Scheduled method added photos from vk album to {@link PhotoCacheService}
     */
    @Scheduled(cron = "0 0 1 * * *")
    private void getPhotoUrl() {
        log.info("Обновление фотогалереи по расписанию");
        try {
            ServiceActor serviceActor = new ServiceActor(Integer.parseInt(APP_ID), CLIENT_ID, SERVICE_TOKEN);
            GetResponse response = vk.photos().get(serviceActor).ownerId(OWNER_ID).albumId(ALBUM_ID).count(1000).execute();
            photoCacheService.addPhotos(response.getItems());
        } catch (ClientException | ApiException e) {
            log.error("Ошибка при получении фотографий с апи вк");
        }
    }
}
