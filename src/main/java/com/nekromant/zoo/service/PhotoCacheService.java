package com.nekromant.zoo.service;

import com.vk.api.sdk.objects.photos.Photo;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoCacheService implements PhotoHolder {
    private List<String> urlPhotos;

    @PostConstruct
    private void init(){
        urlPhotos = new ArrayList<>();
        urlPhotos.add("https://sun1-30.userapi.com/c846520/v846520687/bbb6e/BiBx0iDk1ro.jpg");
    }

    public void addPhotos(List<Photo> photos){
        dropPhotos();
        urlPhotos.addAll(
                photos.stream()
                        .map(photo -> photo.getSizes().get(photo.getSizes().size() - 1).getUrl().toString())
                        .collect(Collectors.toList())
        );
    }

    private void dropPhotos(){
        urlPhotos = new ArrayList<>();
    }

    public List<String> getUrlPhotos() {
        return urlPhotos.subList(0, urlPhotos.size());
    }
}
