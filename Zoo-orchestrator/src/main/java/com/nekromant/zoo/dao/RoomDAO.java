package com.nekromant.zoo.dao;

import com.nekromant.zoo.model.Room;
import enums.AnimalType;
import enums.Location;
import enums.RoomType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomDAO extends CrudRepository<Room, Long> {

    @Query("select distinct r from Room r where r.animalType = :animalType and r.roomType = :roomType and r.videoSupported = :video and r.location = :location")
    List<Room> findAllByParameters(@Param("animalType") AnimalType animalType,
                                   @Param("roomType") RoomType roomType,
                                   @Param("location") Location location,
                                   @Param("video") Boolean videoNeeded);

    @Query("select distinct r from Room r where r.animalType = :animalType and r.roomType = :roomType and r.location = :location")
    List<Room> findAllByParameters(@Param("animalType") AnimalType animalType,
                                   @Param("roomType") RoomType roomType,
                                   @Param("location") Location location);

    @Override
    List<Room> findAll();

    Optional<Room> findById(long id);
}
