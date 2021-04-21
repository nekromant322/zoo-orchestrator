package dto;

import enums.AnimalType;
import enums.Location;
import enums.RequestStatus;
import enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    private Boolean spamRequest;
}
