package com.nekromant.zoo.dao;

import com.nekromant.zoo.telegram.bot.model.TelegramUser;
import org.springframework.data.repository.CrudRepository;

public interface TelegramUserDAO extends CrudRepository<TelegramUser, String> {
    public TelegramUser findByChatId(Long chatId);
}
