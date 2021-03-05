package com.nekromant.zoo.model;

import enums.AnimalType;
import enums.Location;
import enums.RequestStatus;
import enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public AnimalRequest(long l, RequestStatus applied, AnimalType dog, LocalDate of, LocalDate of1, RoomType vip, boolean b, String phoneNumber, String bothify, String name, String name1, String name2, Location moscow) {
    }
}
