package com.nekromant.zoo;

import com.nekromant.zoo.model.AnimalRequest;
import dto.AnimalRequestDTO;
import enums.AnimalType;
import enums.Location;
import enums.RequestStatus;
import enums.RoomType;

import java.time.LocalDate;

public class AnimalRequestUtil {

    private static AnimalRequest createAnimalRequest() {
        return new AnimalRequest(
                1L,
                RequestStatus.NEW,
                AnimalType.DOG,
                LocalDate.now(),
                LocalDate.now(),
                RoomType.VIP,
                true,
                "+7(999)-(999)-(99)-(99)",
                "test@email.com",
                "name",
                "surname",
                "dog-dog",
                100,
                Location.MOSCOW,
                false
        );
    }

    public static AnimalRequest make() {
        return createAnimalRequest();
    }

    public static AnimalRequest createAnimalRequest(boolean spam) {
        AnimalRequest animalRequest = createAnimalRequest();
        animalRequest.setSpamRequest(spam);
        return animalRequest;
    }

    public static AnimalRequest createAnimalRequest(LocalDate beginDate, LocalDate endDate) {
        AnimalRequest animalRequest = createAnimalRequest();
        animalRequest.setBeginDate(beginDate);
        animalRequest.setEndDate(endDate);
        return animalRequest;
    }

    public static AnimalRequest createAnimalRequest(boolean spam, RequestStatus status) {
        AnimalRequest animalRequest = createAnimalRequest();
        animalRequest.setSpamRequest(spam);
        animalRequest.setRequestStatus(status);
        return animalRequest;
    }

    public static AnimalRequest createAnimalRequest(LocalDate beginDate, int price) {
        AnimalRequest animalRequest = createAnimalRequest();
        animalRequest.setBeginDate(beginDate);
        animalRequest.setRequestPrice(price);
        return animalRequest;
    }

    public static AnimalRequestDTO createAnimalRequestDTO() {
        return new AnimalRequestDTO(
                1L,
                RequestStatus.NEW,
                AnimalType.DOG,
                LocalDate.now(),
                LocalDate.now(),
                RoomType.VIP,
                true,
                "79999999999",
                "test@email.com",
                "name",
                "surname",
                "dog-dog",
                Location.MOSCOW,
                false
        );
    }
}
