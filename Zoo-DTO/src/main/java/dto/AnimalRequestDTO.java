package dto;

import enums.AnimalType;
import enums.Location;
import enums.RequestStatus;
import enums.RoomType;
import lombok.*;

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

    public AnimalRequestDTO(
            RequestStatus requestStatus,
            AnimalType animalType,
            LocalDate beginDate,
            LocalDate endDate,
            RoomType roomType,
            String email,
            Boolean videoNeeded,
            String phoneNumber,
            String name,
            String surname,
            String animalName,
            Location location) {
        this.requestStatus = requestStatus;
        this.animalType = animalType;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.roomType = roomType;
        this.email = email;
        this.videoNeeded = videoNeeded;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.surname = surname;
        this.animalName = animalName;
        this.location = location;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class NewAnimalRequestDTO {
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
        private Integer code;
        private Integer price;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class AcceptAnimalRequestDTO {
        private long animalRequestId;
        private long roomId;

    }
}
