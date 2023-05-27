package com.example.telegramBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@PropertySource("application.properties")
@EnableScheduling
@Data
public class BotConfig {
    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;

}
