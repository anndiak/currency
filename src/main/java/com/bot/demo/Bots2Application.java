package com.bot.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class Bots2Application {
    private static final Logger logger = LoggerFactory.getLogger(Bots2Application.class);

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        logger.info("Object TelegramBotsApi has been initialized.");
        try{
            telegramBotsApi.registerBot(new GaretBot());
        } catch (TelegramApiException e){
            logger.error("Error "+e);
        }
//        SpringApplication.run(Bots2Application.class, args);
    }

}
