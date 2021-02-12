package com.nekromant.zoo.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Data
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "authority", unique = true)
    private String authority;

    public Authority(String authority) {
        this.authority = authority;
    }


    @Override
    public String getAuthority() {
        return null;
    }
}