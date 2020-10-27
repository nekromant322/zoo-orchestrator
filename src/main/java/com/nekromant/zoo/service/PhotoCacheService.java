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
        urlPhotos.add("https://i.pinimg.com/originals/19/78/b7/1978b7e72ffaa03e685d261a08d8d53f.jpg");
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
        return urlPhotos.subList(0, urlPhotos.size() - 1);
    }
}
