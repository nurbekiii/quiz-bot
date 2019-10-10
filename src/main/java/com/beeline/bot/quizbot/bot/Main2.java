package com.beeline.bot.quizbot.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

/**
 * @author NIsaev on 18.09.2019
 */
public class Main2 {

    public static void main(String[] args) throws Exception {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        /*try {
            telegramBotsApi.registerBot(new ChannelHandlers());
            telegramBotsApi.registerBot(new DirectionsHandlers());
            telegramBotsApi.registerBot(new RaeHandlers());
            telegramBotsApi.registerBot(new WeatherHandlers());
            telegramBotsApi.registerBot(new TransifexHandlers());
            telegramBotsApi.registerBot(new FilesHandlers());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/
    }
}
