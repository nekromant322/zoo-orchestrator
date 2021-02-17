package com.cko.zoo.dto;

import com.cko.zoo.enums.AnimalType;
import com.cko.zoo.enums.Location;
import com.cko.zoo.enums.RequestStatus;
import com.cko.zoo.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AnimalRequestDTO {
    private Long id;

    private RequestStatus requestStatus;

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
