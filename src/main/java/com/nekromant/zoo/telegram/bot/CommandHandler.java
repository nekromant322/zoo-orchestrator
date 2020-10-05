package com.nekromant.zoo.telegram.bot;

import com.nekromant.zoo.dao.TelegramUserDAO;
import com.nekromant.zoo.enums.RoomType;
import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.service.AnimalRequestService;
import com.nekromant.zoo.service.PriceService;
import com.nekromant.zoo.telegram.bot.commands.OrderRoomChoiceCommand;
import com.nekromant.zoo.telegram.bot.model.TelegramUser;
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
    private AnimalRequestService animalRequestService;

    @Autowired
    private TelegramUserDAO telegramUserDAO;

    @Autowired
    private PriceService priceService;

    public SendMessage getSendMessage(Update update){
        String message = getMessageOrCallbackQuery(update);
        switch (message) {
            case (START):
                createTelegramUserIfNotExist(update.getMessage().getChatId().longValue());
                return getStartUpMsg(update);
            case (ORDER):
                return getOrderRoomChoiceMenu(update);
            default:
                if (message.startsWith(OrderRoomChoiceCommand.ORDER_ROOM_PREFIX)) {
                    setUpRoomTypeForUserRequest(update);
                }

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
        sendMessage.setChatId(getChatId(update));
        sendMessage.setText(UNKNOWN_COMMAND_MESSAGE);
        return sendMessage;
    }

    private void createTelegramUserIfNotExist(Long chatId){
        if (telegramUserDAO.findByChatId(chatId) == null) {
            AnimalRequest animalRequest = animalRequestService.insert(new AnimalRequest());
            TelegramUser user = new TelegramUser(chatId, animalRequest);
            telegramUserDAO.save(user);
        }
    }

    private String getMessageOrCallbackQuery(Update update){
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData();
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            return update.getMessage().getText();
        } else {
            return "UNKNOWN_MEDIA";
        }
    }

    //TODO: better handle ways where we can get chatId. Now we cover only message and callback ways
    private String getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId().toString();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId().toString();
        } else {
            //TODO: bad practice!!!
            return null;
        }
    }

    private void setUpRoomTypeForUserRequest(Update update){
        long animalRequestIdForUser = telegramUserDAO.findByChatId(update.getCallbackQuery().getMessage().getChatId())
                .getAnimalRequestId().getId();
        animalRequestService.updateRoomTypeByRequestId(
                animalRequestIdForUser,
                RoomType.valueOf(update.getCallbackQuery().getData().toString().split(OrderRoomChoiceCommand.ORDER_ROOM_DELIMETER)[1]));
    }
}
