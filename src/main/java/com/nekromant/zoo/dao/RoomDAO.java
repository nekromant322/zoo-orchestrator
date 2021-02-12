package com.nekromant.zoo.dao;

import com.cko.zoo.enums.AnimalType;
import com.cko.zoo.enums.RoomType;
import com.nekromant.zoo.model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomDAO extends CrudRepository<Room,Long> {

    @Query("select distinct r from Room r where r.animalType = :animalType and r.roomType = :roomType and r.videoSupported = :video")
    List<Room> findAllByParametrs(@Param("animalType") AnimalType animalType,
                                      @Param("roomType") RoomType roomType,
                                      @Param("video") Boolean videoNeeded);

    @Override
    List<Room> findAll();
}
