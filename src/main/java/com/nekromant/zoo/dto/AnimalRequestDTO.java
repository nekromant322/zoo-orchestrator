package com.nekromant.zoo.dto;

import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.Location;
import com.nekromant.zoo.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AnimalRequestDTO {
    private long id;

    private AnimalType animalType;

    private LocalDate beginDate;

    private LocalDate endDate;

    private RoomType roomType;

    private Boolean videoNeeded;

    private String phoneNumber;

    private String email;

    private String name;

    private String surname;

    private String animalName;

    private Location location;

    private boolean banned;
}
