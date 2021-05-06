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
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String token;

    @Column
    private String email;

    @Column
    private LocalDate expiredDate;

    public ConfirmationToken(String token, String email, LocalDate expiredDate) {
        this.token = token;
        this.email = email;
        this.expiredDate = expiredDate;
    }
}
