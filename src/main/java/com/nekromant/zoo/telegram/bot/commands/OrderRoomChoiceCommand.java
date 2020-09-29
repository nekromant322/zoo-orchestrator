package com.nekromant.zoo.telegram.bot.commands;

import com.nekromant.zoo.service.PriceService;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class OrderRoomChoiceCommand extends TelegramBotCommand {

    private PriceService priceService;

    public OrderRoomChoiceCommand(PriceService priceService) {
        this.priceService = priceService;
    }

    public ReplyKeyboardMarkup getResponseMenu() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("We have rooms for this price");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add("Common room: " + priceService.getActualPrice().getCommonRoomPrice());
        keyboardRows.add(firstRow);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add("Large room: " + priceService.getActualPrice().getLargeRoomPrice());
        keyboardRows.add(secondRow);

        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add("VIP room: " + priceService.getActualPrice().getVipRoomPrice());
        keyboardRows.add(thirdRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
}
