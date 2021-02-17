package com.cko.zoo.dto;

import com.cko.zoo.enums.AnimalType;
import com.cko.zoo.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RoomParametersDTO {
    private AnimalType animalType;
    private RoomType roomType;
    private boolean videoSupported;
    private LocalDate begin;
    private LocalDate end;
}
