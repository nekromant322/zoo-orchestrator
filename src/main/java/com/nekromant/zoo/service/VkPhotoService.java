package com.nekromant.zoo.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.responses.GetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
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

    private VkApiClient vk = new VkApiClient(new HttpTransportClient());

    public void getPhotoUrl() {
        try {
            ServiceActor serviceActor = new ServiceActor(Integer.parseInt(APP_ID), CLIENT_ID, SERVICE_TOKEN);
            GetResponse response = vk.photos().get(serviceActor).ownerId(OWNER_ID).albumId(ALBUM_ID).count(1000).execute();
            photoCacheService.addPhotos(response.getItems());
        } catch (ClientException | ApiException e){

        }
    }
}
