package com.nekromant.zoo.telegram.bot;

import com.nekromant.zoo.service.PriceService;
import com.nekromant.zoo.telegram.bot.commands.OrderRoomChoiceCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

@Service
public class CommandHandler {

    private static final String START = "/start";
    private static final String ORDER = "/order";

    //TODO: change stub messages to normal one
    private static final String START_MESSAGE = "Put start message here. For order send /order";
    private static final String ORDER_MESSAGE = "Put order message here";
    private static final String UNKNOWN_COMMAND_MESSAGE = "Unknown command";

    @Autowired
    private PriceService priceService;

    public SendMessage getSendMessage(Update update){
        String message = update.getMessage().getText();
        switch (message) {
            case (START):
                return getStartUpMsg(update);
            case (ORDER):
                return getOrderRoomChoiceMenu(update);
        }
        return getUnknownCommandMessage(update);
    }

    private SendMessage getStartUpMsg(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(START_MESSAGE);
        return sendMessage;
    }

    private SendMessage getOrderRoomChoiceMenu(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(ORDER_MESSAGE);
        OrderRoomChoiceCommand orderRoomChoiceCommand = new OrderRoomChoiceCommand(priceService);
        sendMessage.setReplyMarkup(orderRoomChoiceCommand.getResponseMenu());
        return sendMessage;
    }

    private SendMessage getUnknownCommandMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(UNKNOWN_COMMAND_MESSAGE);
        return sendMessage;
    }
}
