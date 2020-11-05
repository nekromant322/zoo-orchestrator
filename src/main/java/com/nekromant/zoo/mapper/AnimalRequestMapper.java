package com.nekromant.zoo.mapper;

import com.nekromant.zoo.dto.AnimalRequestDTO;
import com.nekromant.zoo.model.AnimalRequest;
import org.springframework.stereotype.Component;

@Component
public class AnimalRequestMapper {
    public AnimalRequestDTO entityToDto(AnimalRequest animalRequest){
        return new AnimalRequestDTO(
                animalRequest.getId(),
                animalRequest.getAnimalType(),
                animalRequest.getBeginDate(),
                animalRequest.getEndDate(),
                animalRequest.getRoomType(),
                animalRequest.getVideoNeeded(),
                animalRequest.getPhoneNumber(),
                animalRequest.getEmail(),
                animalRequest.getName(),
                animalRequest.getSurname(),
                animalRequest.getAnimalName(),
                animalRequest.getLocation(),
                false
        );
    }
}
