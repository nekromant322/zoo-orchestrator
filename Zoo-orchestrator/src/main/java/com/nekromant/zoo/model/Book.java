package com.nekromant.zoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long animalRequestId;

    @Column
    private long roomId;

    @Column(columnDefinition = "DATE")
    private LocalDate beginDate;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    public Book(long animalRequestId, long roomId, LocalDate beginDate, LocalDate endDate) {
        this.animalRequestId = animalRequestId;
        this.roomId = roomId;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}
