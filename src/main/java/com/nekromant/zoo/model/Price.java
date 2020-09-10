package com.nekromant.zoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Цены разных видов услуг за один день обслуживания
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer commonRoomPrice;

    @Column
    private Integer largeRoomPrice;

    @Column
    private Integer vipRoomPrice;

    @Column
    private Integer videoPrice;

    @Column
    private LocalDateTime lastUpdated;

}
