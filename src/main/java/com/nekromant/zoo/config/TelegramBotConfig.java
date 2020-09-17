package com.nekromant.zoo.config;

import com.nekromant.zoo.telegram.bot.ZooBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Configuration
public class TelegramBotConfig {

    static {
        ApiContextInitializer.init();
    }

    @Autowired
    private ZooBot zooBot;

    @PostConstruct
    public void start() {
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(zooBot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
