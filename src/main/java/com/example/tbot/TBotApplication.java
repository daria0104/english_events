package com.example.tbot;

import com.example.tbot.config.BotConfig;
import com.example.tbot.service.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class TBotApplication {

    public static void main(String[] args) {
//        BotConfig.loadBotSettings();
//        ApiContextInitializer.init();
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        try {
//            telegramBotsApi.registerBot(new TelegramBot(new BotConfig()));
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
        SpringApplication.run(TBotApplication.class, args);
    }

}
