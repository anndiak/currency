package com.bot.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class GaretBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(GaretBot.class);
    ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
    String currency = null;
    private long chat_id;
    Page page;

    @Override
    public void onUpdateReceived(Update update) {
        chat_id = update.getMessage().getChatId();

        SendMessage message = new SendMessage();

                    message.setChatId(chat_id)
                    .setText(getMessage(update.getMessage().getText(), update.getMessage().getFrom().getFirstName(),
                            update.getMessage()));
                    message.setReplyMarkup(markup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error "+e);
        }
    }


    public String getMessage(String msg, String name, Message message) {
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow firstRow = new KeyboardRow();
        KeyboardRow secondRow = new KeyboardRow();

        markup.setSelective(true);
        markup.setResizeKeyboard(true);

        if ( msg.equals("/start")) {
            logger.info("/start was selected");

            Long id = message.getFrom().getId().longValue();

            UsersData usersData1 = new UsersData();
            if(usersData1.getUser(id).getId() == null){
                UsersData usersData2 = new UsersData();
                String lastName = message.getFrom().getLastName();
                if(lastName == null){
                    lastName = "";
                }
                usersData2.addUser(id,message.getFrom().getFirstName(),lastName);
                logger.info("New user was added to DB");
            }

            rows.clear();
            firstRow.add("EUR/UAH");
            firstRow.add("USD/UAH");
            firstRow.add("RUB/UAH");

            rows.add(firstRow);

            markup.setKeyboard(rows);
            logger.info("Displaying greeting text.");
            return "Добрый день, " + name + "!\n"+
                    "Выберите валюту, которую будем искать" ;
        }

        if (msg.equals("EUR/UAH") || msg.equals("USD/UAH") || msg.equals("RUB/UAH")) {
            logger.info("Menu "+ msg +" SELECTED.");
            switch (msg){
                case "EUR/UAH":
                    currency = "EUR";
                    break;
                case "USD/UAH":
                    currency = "USD";
                    break;
                case "RUB/UAH":
                    currency = "RUB";
                    break;
                default:
            }
            rows.clear();
            firstRow.add("Ощадбанк");
            firstRow.add("ПриватБанк");
            firstRow.add("money24.kharkov.ua");
            secondRow.add("Валюта");

            rows.add(firstRow);
            rows.add(secondRow);

            markup.setKeyboard(rows);
            return "Выберите источник данных:";
        }

        if(msg.equals("Ощадбанк") && currency != null) {
            logger.info("Menu "+ msg +" SELECTED.");
            return getCurrencyOfBank("https://www.oschadbank.ua/ua/private/currency");
        }

        if(msg.equals("ПриватБанк") && currency != null) {
            logger.info("Menu "+ msg +" SELECTED.");
            return getCurrencyOfBank("https://privatbank.ua/");
        }

        if(msg.equals("money24.kharkov.ua") && currency != null){
            logger.info("Menu "+ msg +" SELECTED.");
            return getCurrencyOfBank("https://money24.kharkov.ua/");
        }

        if(msg.equals("Валюта")){
            logger.info("Menu of currency is displayed.");
            rows.clear();
            firstRow.add("EUR/UAH");
            firstRow.add("USD/UAH");
            firstRow.add("RUB/UAH");

            rows.add(firstRow);

            markup.setKeyboard(rows);
            return "Выберите валюту, которую будем искать:";
        }

        return "Если возникли проблемы, воспользуйтесь /start";
    }


    private String getCurrencyOfBank(String href){
        logger.info("Getting data of selected currency");
        page = new Page(href, currency);
        return page.getList().toString();
    }


    @Override
    public String getBotUsername() {
        return "@TestGareBot";
    }

    @Override
    public String getBotToken() {
        return "1540154117:AAFKqdyo7mM3pPXwzZAvqwFXuY2mfSsPx8A";
    }

}
