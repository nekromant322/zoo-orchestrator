package com.nekromant.zoo.service;

import com.nekromant.zoo.dao.RoomDAO;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomDAO roomDAO;

    public void insert(Room room){
        roomDAO.save(room);
    }

    public List<Room> findByAnimalRequest(AnimalRequest animalRequest){
        return roomDAO.findAllByAnimalRequest(
                animalRequest.getAnimalType(),
                animalRequest.getRoomType(),
                animalRequest.getVideoNeeded()
        );
    }


}
