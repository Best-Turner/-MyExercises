package com.example.telegramBot.services;

import com.example.telegramBot.config.BotConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Log4j2
public class TelegramBot extends TelegramLongPollingBot {

    private static final String HELP_TEXT = "команды из главного меню слево или набрать команды вручную\n" +
            "Нажми /start чтобы увидеть приветственное сообщение\n" +
            "Нажми /mydata чтоб посмотреть данные о себе\n" +
            "Нажми /help для получения справки";
    private static final List<String> COMMANDS = Arrays.asList("/start", "/help", "/mydata");
    private final BotConfig botConfig;
    private final UserService userService;


    public TelegramBot(BotConfig botConfig, UserService userService) {
        this.botConfig = botConfig;
        this.userService = userService;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "начало"));
        commands.add(new BotCommand("/help", "начало"));
        commands.add(new BotCommand("/mydata", "начало"));
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        var message = update.getMessage();
        if (message.hasText()) {
            Long chatId = message.getChat().getId();
            readingCommands(chatId, message);

        } else if (update.hasCallbackQuery()) {

        }
    }

    private void readingCommands(Long chatId, Message message) {
        String command = message.getText();
        switch (command) {
            case "/start" -> sendText(chatId, "Hello " + message.getFrom().getFirstName());
            case "/help" -> sendText(chatId, HELP_TEXT);
            case "/mydata" -> returnSimpleDataToUser(chatId, message);
            default -> listCommand(chatId, command);
        }
    }

    private void returnSimpleDataToUser(Long chatId, Message message) {
        log.info("Пользователь запросил свои данные!");
        StringBuilder builder = new StringBuilder();
        var user = message.getFrom();
        builder.append("Имя пользователя - " + user.getUserName() + "\n")
                .append("Имя - " + user.getFirstName() + "\n")
                .append("Премиум - " + (user.getIsPremium() == null ? "нет" : "да"));
        sendText(chatId, builder.toString());
    }

    private void listCommand(Long chatId, String incorrectCommand) {
        StringBuilder builder = new StringBuilder();
        COMMANDS.forEach(command -> builder.append(command + "\n"));
        log.info("Введена не корректная комманда " + incorrectCommand);
        sendText(chatId, "Список доступных комманд\n" + builder);
    }

    private void sendText(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
            log.info("Сообщение отправленно");
        } catch (TelegramApiException e) {
            log.error("Сообщение не отпраленно " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
