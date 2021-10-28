package com.nekromant.zoo.model;

import enums.MailingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailingReceiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "topic")
    private String topic;

    @Column(name = "text")
    private String text;

    @Column(name = "type")
    private MailingType type;

    public MailingReceiver(String phoneNumber, String email, String topic, String text, MailingType type) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.topic = topic;
        this.text = text;
        this.type = type;
    }
}
