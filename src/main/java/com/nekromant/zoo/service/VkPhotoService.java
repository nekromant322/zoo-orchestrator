package com.nekromant.zoo.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.responses.GetResponse;
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

    private VkApiClient vk = new VkApiClient(new HttpTransportClient());

    public List<String> getPhotoUrl() throws ClientException, ApiException {
        ServiceActor serviceActor = new ServiceActor(Integer.parseInt(APP_ID), CLIENT_ID,SERVICE_TOKEN);
        GetResponse response = vk.photos().get(serviceActor).ownerId(OWNER_ID).albumId(ALBUM_ID).count(5).execute();
        List<Photo> photoList = response.getItems();
        List<String> photosUrl = new ArrayList<>();
        for (int i = 0; i < photoList.size();i++) {
            photosUrl.add(photoList.get(i).getSizes().get(photoList.get(i).getSizes().size()-1).getUrl().toString());
        }
        return photosUrl;
    }
}
