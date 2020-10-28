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
    private static final String PHOTO_1 = "/img/defaultPhotogalleryImg/1.jpg";
    private static final String PHOTO_2 = "/img/defaultPhotogalleryImg/2.jpg";

    /**
     * PostConstruct method added default photos before first load from vk api
     */
    @PostConstruct
    private void init(){
        urlPhotos = new ArrayList<>();
        urlPhotos.add(PHOTO_1);
        urlPhotos.add(PHOTO_2);
    }

    /**
     * Method drop old cache and put new photos from vk api
     * @param photos - list of photos
     */
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

    /**
     * Implementation of {@link PhotoHolder} interface to get photos
     * @return sub list from cache
     */
    public List<String> getUrlPhotos() {
        return urlPhotos.subList(0, urlPhotos.size());
    }
}
