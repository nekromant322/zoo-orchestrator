package com.nekromant.zoo.dto;

import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RoomParametersDTO {
    private AnimalType animalType;
    private RoomType roomType;
    private boolean videoNeeded;
    private LocalDate begin;
    private LocalDate end;
}
