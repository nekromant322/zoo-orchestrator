package com.nekromant.zoo.telegram.bot.model;

import com.nekromant.zoo.model.AnimalRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TelegramUser {
    @Id
    private Long chatId;
    @OneToOne
    private AnimalRequest animalRequestId;
}
