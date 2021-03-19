package com.nekromant.zoo.mapper;

import com.nekromant.zoo.domain.BookParams;
import dto.RoomDTO;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookParams bookParamsFromDto(String animalRequestId, RoomDTO roomDTO){
        return new BookParams(
                Long.parseLong(animalRequestId),
                roomDTO.getId(),
                roomDTO.getAnimalType(),
                roomDTO.getRoomType(),
                roomDTO.getVideoSupported(),
                roomDTO.getBegin().toString(),
                roomDTO.getEnd().toString()
        );
    }
}
