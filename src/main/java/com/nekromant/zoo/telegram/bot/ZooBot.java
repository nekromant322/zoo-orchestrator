package com.nekromant.zoo.telegram.bot;

import com.nekromant.zoo.model.AnimalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

@Component
@Profile("Telegram-bot")
public class ZooBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    @Autowired
    private CommandHandler commandHandler;

    private AnimalRequest request;

    @Override
    public void onClosing() {

    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        sendMsg(commandHandler.getSendMessage(update));
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        for(Update update : updates) {
            onUpdateReceived(update);
        }
    }

    private synchronized void sendMsg(SendMessage message) {
        try{
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}
