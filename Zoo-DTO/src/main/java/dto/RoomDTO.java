package dto;

import enums.AnimalType;
import enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {

    private long id;

    private AnimalType animalType;

    private RoomType roomType;

    private Boolean videoSupported;

    private String description;

    private LocalDate begin;

    private LocalDate end;
}
