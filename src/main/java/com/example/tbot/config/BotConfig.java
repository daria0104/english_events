package com.example.tbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@Data
@EnableScheduling
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.key}")
    String token;

    @Value("${bot.owner}")
    Long ownerId;

//    public static final String BOT_FILE = "src/main/resources/application.properties";
//
//    public static String BOT_NAME;
//    public static String BOT_TOKEN;
//    public static void loadBotSettings() {
//
//        Properties bot = new Properties();
//
//        try (InputStream is = new FileInputStream(BOT_FILE)) {
//            bot.load(is);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        BOT_NAME = bot.getProperty("bot.name");
//        BOT_TOKEN = bot.getProperty("bot.token");
//    }
}
