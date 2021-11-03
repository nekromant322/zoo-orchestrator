package com.nekromant.zoo.model;

import enums.AnimalType;
import enums.Location;
import enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private AnimalType animalType;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column
    private Location location;

    @Column
    private Boolean videoSupported;

    @Column
    private String description;
}
