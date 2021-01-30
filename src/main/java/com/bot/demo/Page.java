package com.bot.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class Page {
    private static final Logger logger = LoggerFactory.getLogger(Page.class);
    private Document document;
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public Page(String href, String valute){
        logger.info("Page`s constructor have been run.");
        connect(href, valute);
    }
    //подключаемся к странице

    private void connect(String href, String valute){
        try{
            String currency = null;
            logger.info("Connecting to the page.");
            document = Jsoup.connect(href).get();

            logger.info("Getting value of all td-tags.");
            Elements elements = document.select("tbody > tr > td:not(:has(*))");
            for (int i = 0; i < elements.size();i++) {
                Element e = elements.get(i);

                switch (e.select("td").html()) {
                    case "USD":
                    case "Американский доллар / Украинская гривна":
                        currency = "USD";
                        break;
                    case "EUR":
                    case "Евро / Украинская гривна":
                        currency = "EUR";
                        break;
                    case "RUB":
                    case "Российский рубль / Украинская гривна":
                        currency = "RUB";
                        break;
                }

                if((e.select("td").html()).contains(".") && !isNumeric(elements.get(i+2).select("td").html())  && currency != null && currency.equals(valute)){
                    list = new ArrayList<>();
                    list.add(e.select("td").html());
                    list.add(elements.get(i+1).select("td").html());

                    if(list.size() == 2){
                        logger.info("Added data in arrayList about {}",currency);
                        break;
                    }
                }
            }
        }catch (IOException e){
            logger.error("Error "+e);
        }
    }



    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
