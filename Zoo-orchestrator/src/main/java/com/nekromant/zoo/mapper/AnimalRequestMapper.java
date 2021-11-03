package com.nekromant.zoo.mapper;

import com.nekromant.zoo.model.AnimalRequest;
import dto.AnimalRequestDTO;
import org.springframework.stereotype.Component;

@Component
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
                animalRequest.getSpamRequest()
        );
    }

    public AnimalRequest dtoToEntity(AnimalRequestDTO animalRequestDTO, int requestPrice){
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
                requestPrice,
                animalRequestDTO.getLocation(),
                animalRequestDTO.getSpamRequest()
        );
    }

    public AnimalRequestDTO newAnimalRequestDTOToAnimalRequestDTO(AnimalRequestDTO.NewAnimalRequestDTO requestDTO) {
        return new AnimalRequestDTO(
                requestDTO.getRequestStatus(),
                requestDTO.getAnimalType(),
                requestDTO.getBeginDate(),
                requestDTO.getEndDate(),
                requestDTO.getRoomType(),
                requestDTO.getEmail(),
                requestDTO.getVideoNeeded(),
                requestDTO.getPhoneNumber(),
                requestDTO.getName(),
                requestDTO.getSurname(),
                requestDTO.getAnimalName(),
                requestDTO.getLocation()
        );
    }
}
