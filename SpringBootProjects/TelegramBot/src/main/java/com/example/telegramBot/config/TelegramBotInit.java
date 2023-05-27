package com.example.telegramBot.config;

import com.example.telegramBot.services.TelegramBot;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Log4j2
public class TelegramBotInit {
    @Autowired
    private final TelegramBot telegramBot;

    public TelegramBotInit(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }


    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
            log.info("Телеграм бот зарегистрирован");
        } catch (TelegramApiException e) {
            log.error("Телеграмм бот не зарегистрирован " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
