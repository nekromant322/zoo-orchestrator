package com.cko.zoo.dto;

import com.cko.zoo.enums.AnimalType;
import com.cko.zoo.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomDTO {
    private Long id;
    private AnimalType animalType;
    private RoomType roomType;
    private Boolean videoSupported;

}
