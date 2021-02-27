package com.nekromant.zoo.domain;

import enums.AnimalType;
import enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookParams {
    private Long animalRequestId;

    private Long id;

    private AnimalType animalType;

    private RoomType roomType;

    private Boolean videoSupported;

    private String begin;

    private String end;
}
