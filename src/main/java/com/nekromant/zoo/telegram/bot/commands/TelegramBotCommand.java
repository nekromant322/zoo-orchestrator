package com.nekromant.zoo.telegram.bot.commands;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

public abstract class TelegramBotCommand {

    public abstract InlineKeyboardMarkup getResponseMenu();
}
