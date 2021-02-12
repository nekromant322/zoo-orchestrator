package com.nekromant.zoo.mapper;

import com.cko.zoo.dto.AnimalRequestDTO;
import com.nekromant.zoo.model.AnimalRequest;
import org.springframework.stereotype.Service;

@Service
public class AnimalRequestMapper {
    public AnimalRequestDTO entityToDto(AnimalRequest animalRequest){
        return new AnimalRequestDTO(
                animalRequest.getId(),
                animalRequest.getRequestStatus(),
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

    public AnimalRequest dtoToEntity(AnimalRequestDTO animalRequestDTO){
        return new AnimalRequest(
                animalRequestDTO.getId(),
                animalRequestDTO.getRequestStatus(),
                animalRequestDTO.getAnimalType(),
                animalRequestDTO.getBeginDate(),
                animalRequestDTO.getEndDate(),
                animalRequestDTO.getRoomType(),
                animalRequestDTO.getVideoNeeded(),
                animalRequestDTO.getPhoneNumber(),
                animalRequestDTO.getEmail(),
                animalRequestDTO.getName(),
                animalRequestDTO.getSurname(),
                animalRequestDTO.getAnimalName(),
                animalRequestDTO.getLocation()
        );
    }
}
