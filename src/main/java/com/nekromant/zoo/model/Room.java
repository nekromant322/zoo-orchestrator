package com.nekromant.zoo.model;

import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    private Boolean videoSupported;

    @Column
    private String description;

    @Column
    private LocalDate endDate;
}
