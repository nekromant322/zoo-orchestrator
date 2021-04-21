package com.nekromant.zoo.model;

import enums.AnimalType;
import enums.Location;
import enums.RequestStatus;
import enums.RoomType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Column
    private int requestPrice;

    @Enumerated(EnumType.STRING)
    private Location location;

    @Column
    private Boolean spamRequest;
}
