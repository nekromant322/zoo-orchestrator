package com.nekromant.zoo.model;

import enums.Discount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<Authority> authorities;

    @Enumerated(EnumType.STRING)
    private Discount discount = Discount.NONE;


    public User(String email, String password, List<Authority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public User(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
