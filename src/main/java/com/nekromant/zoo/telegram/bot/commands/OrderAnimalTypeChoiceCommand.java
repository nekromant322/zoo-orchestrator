package com.nekromant.zoo.telegram.bot.commands;

import com.nekromant.zoo.enums.AnimalType;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


public class OrderAnimalTypeChoiceCommand extends TelegramBotCommand {

    public final static String ORDER_ANIMAL_PREFIX = "/order_animal";
    public final static String ORDER_ANIMAL_DELIMITER = ":";

    @Override
    public InlineKeyboardMarkup getResponseMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for(AnimalType animalType : AnimalType.values()) {
            addNewAnimalInKeyboard(inlineKeyboardMarkup, animalType);
        }
        return inlineKeyboardMarkup;
    }

    private void addNewAnimalInKeyboard(InlineKeyboardMarkup keyboard, AnimalType animalType) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton(animalType.name());
        button.setCallbackData(ORDER_ANIMAL_PREFIX + ORDER_ANIMAL_DELIMITER + animalType.name());
        row.add(button);
        keyboard.getKeyboard().add(row);
    }
}
