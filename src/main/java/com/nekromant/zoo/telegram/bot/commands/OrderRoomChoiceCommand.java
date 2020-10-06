package com.nekromant.zoo.telegram.bot.commands;

import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.service.PriceService;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class OrderRoomChoiceCommand extends TelegramBotCommand {

    public final static String ORDER_ROOM_PREFIX = "/order_room";
    public final static String ORDER_ROOM_DELIMITER = ":";

    private PriceService priceService;

    public OrderRoomChoiceCommand(PriceService priceService) {
        this.priceService = priceService;
    }

    public InlineKeyboardMarkup getResponseMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        addNewOneButtonWidthRowToKeyboard(inlineKeyboardMarkup,
                "Common room: " + priceService.getActualPrice().getCommonRoomPrice(),
                ORDER_ROOM_PREFIX + ORDER_ROOM_DELIMITER + RoomType.COMMON.name()
                );
        addNewOneButtonWidthRowToKeyboard(inlineKeyboardMarkup,
                "Large room: " + priceService.getActualPrice().getLargeRoomPrice(),
                ORDER_ROOM_PREFIX + ORDER_ROOM_DELIMITER + RoomType.LARGE.name()
        );
        addNewOneButtonWidthRowToKeyboard(inlineKeyboardMarkup,
                "VIP room: " + priceService.getActualPrice().getVipRoomPrice(),
                ORDER_ROOM_PREFIX + ORDER_ROOM_DELIMITER + RoomType.VIP.name()
        );
        return inlineKeyboardMarkup;
    }

    private void addNewOneButtonWidthRowToKeyboard(InlineKeyboardMarkup keyboard, String buttonText, String callbackText) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton(buttonText);
        button.setCallbackData(callbackText);
        row.add(button);
        keyboard.getKeyboard().add(row);
    }
}
