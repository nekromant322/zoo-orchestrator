package com.nekromant.zoo.model;

import com.nekromant.zoo.enums.RequestStatus;
import com.nekromant.zoo.enums.AnimalType;
import com.nekromant.zoo.enums.Location;
import com.nekromant.zoo.enums.RoomType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class AnimalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    
    @Enumerated(EnumType.STRING)
    private AnimalType animalType;

    @Column
    private LocalDate beginDate;

    @Column
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column
    private Boolean videoNeeded;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private String animalName;

    @Enumerated(EnumType.STRING)
    private Location location;
}
