package com.beeline.bot.quizbot.bot;

import com.beeline.bot.quizbot.entity.Client;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.passport.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NIsaev on 18.09.2019
 */
public class Main4 {

    private List<Client> clientList;

    public static void main(String[] args) {

        Main4 mn = new Main4();
        //create a new Telegram bot object to start talking with Telegram
        String BOT_TOKEN = "940004167:AAH7DbyZ6H4bJDrp6jx8aMdpWcVXlg804nM";
        final long CHAT_ID = 508363280;
        TelegramBot bot = new TelegramBot(BOT_TOKEN);

        // Register for updates
        bot.setUpdatesListener(updates -> {
            if (updates != null) {
                for (Update update : updates) {
                    Message message = update.message();
                    InlineQuery inlineQuery = update.inlineQuery();
                    CallbackQuery callbackQuery = update.callbackQuery();
                    Contact contact = message.contact();
                    if (message != null) {
                        String text = message.text();

                        long chatId = update.message().chat().id();
                        Chat chat = update.message().chat();
                        Client clnt = mn.checkClientInList(new Client(chatId, chat.username(), chat.firstName(), chat.lastName()));

                        if (message.replyToMessage() != null) {
                            if (clnt.getClntFullName() == null && message.replyToMessage().messageId().equals(clnt.getAskNameMessId())) {
                                clnt.setClntFullName(message.text()); //FIRST CHANGE
                                clnt.setPoints(150);

                                clnt = mn.changeProps(clnt);

                                String msg22 = "Поздравляем, ты заработал 50 баллов";
                                SendResponse response22 = bot.execute(new SendMessage(chatId, msg22));

                                String msg33 = "Для того чтобы ты мог понять суть игры, предлагаем тебе пройти первое задание. Пройди по ссылке и пройди онлайн тренинг на нашей корпоративной платформе Crossknowledge и ознакомься с ним. Для подтверждения выполнения задания загрузи скрин со 100% прогрессом по курсу.";
                                SendMessage send33 = new SendMessage(chatId, msg33);

                                ////????????????
                                send33.replyMarkup(new ForceReply());
                                SendResponse response33 = bot.execute(send33);
                                int messId = response33.message().messageId();

                                clnt.setAskTask1MessId(messId); //SECOND CHANGE
                                clnt = mn.changeProps(clnt);
                            } else if (clnt.getClntFullName() != null && message.replyToMessage().messageId().equals(clnt.getAskTask1MessId())) {
                                //Document doc = message.document();
                                PhotoSize[] sizes = message.photo();
                                clnt.setTask1FielId(sizes[0].fileId());
                                clnt.setPoints(clnt.getPoints() + 200);
                                clnt = mn.changeProps(clnt); //THIRD CHANGE

                                //SendPhoto photo = new SendPhoto(chatId, sizes[0].fileId());
                                //bot.execute(photo);

                                String msg44 = "Поздравляем! +200 баллов к твоему счету баллов. Для того чтобы продолжить игру, выбери компетенцию";
                                SendMessage send44 = new SendMessage(chatId, msg44);

                                Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                                        new String[]{"Фокус на клиента", "Лидерство и развитие команд"},
                                        new String[]{"Стратегическое мышление", "Открытость к экспериментам"},
                                        new String[]{"Бизнес-проницательность", "Нацеленность на результат"},
                                        new String[]{"Управление изменениями", "Честность и этика"},
                                        new String[]{"Сотрудничество"})
                                        //.oneTimeKeyboard(true)   // optional
                                        .resizeKeyboard(true)    // optional
                                        .selective(true);        // optional
                                SendResponse response4 = bot.execute(send44.replyMarkup(replyKeyboardMarkup));
                                int messId = response4.message().messageId();
                            }

                        }
                        if (text != null) {
                            switch (text) {
                                case "/start":
                                    String msg = "Добро пожаловать в игру Кубок компетенций или BeeHero! Have fun and become winner. Впереди тебя ждет много увлекательных приключений и новой информации, которые помогут тебе быть эффективными в твоей роли.";
                                    SendResponse response1 = bot.execute(new SendMessage(chatId, msg));

                                    String msg2 = "Нажмите на /register для регистрации в системе";
                                    SendResponse response2 = bot.execute(new SendMessage(chatId, msg2));
                                    break;

                                case "/register":
                                    mn.setPoints(clnt, 100);
                                    String msg3 = "Мы поздравляем тебя! Ты уже заработал свои первые 100 баллов. Чтобы ты мог продолжать зарабатывать баллы далее, предлагаю познакомиться";
                                    SendResponse response3 = bot.execute(new SendMessage(chatId, msg3));

                                    String msg4 = "Пожалуйста введите Ваши Имя и Фамилию";
                                    SendMessage send4 = new SendMessage(chatId, msg4);

                                    send4.replyMarkup(new ForceReply());
                                    SendResponse response4 = bot.execute(send4);
                                    int messId = response4.message().messageId();

                                    clnt.setAskNameMessId(messId);
                                    mn.changeProps(clnt);

                                    break;
                                case "/smile":
                                    break;

                                case "/cry":
                                    break;

                                case "Фокус на клиента":
                                    String msg5 = "Вы выбрали курс '" + text + "', отправляем Вам задание.\n\r" + " Ваше задание сделать XXXXXXXXXX";
                                    SendResponse response5 = bot.execute(new SendMessage(chatId, msg5));
                                    break;
                                case "Лидерство и развитие команд":
                                    String msg6 = "Вы выбрали курс '" + text + "', отправляем Вам задание.\n\r" + " Ваше задание сделать YYYYYYYY";
                                    SendResponse response6 = bot.execute(new SendMessage(chatId, msg6));
                                    break;
                                case "Стратегическое мышление":
                                    String msg7 = "Вы выбрали курс '" + text + "', отправляем Вам задание.\n\r" + " Ваше задание сделать ZZZZZZZ";
                                    SendResponse response7 = bot.execute(new SendMessage(chatId, msg7));
                                    break;
                            }
                        }
                        System.out.println("---------------\n\r");
                        System.out.println(chat);
                        System.out.println(text != null ? text : "");
                        System.out.println(message.messageId());
                        System.out.println("---------------\n\r");
                    }

                    if (inlineQuery != null) {
                        System.out.println("-------inlineQuery--------\n\r");
                        String query = inlineQuery.query();
                        System.out.println(inlineQuery.from().toString());
                        System.out.println(query);
                        System.out.println("-------inlineQuery--------\n\r");
                    }

                    if (callbackQuery != null) {
                        System.out.println("-------callbackQuery--------\n\r");
                        String query = callbackQuery.data();
                        System.out.println(callbackQuery.from().toString());
                        System.out.println(query);
                        System.out.println("-------callbackQuery--------\n\r");
                    }

                    if (contact != null) {
                        System.out.println("-------contact--------\n\r");
                        String result = contact.toString();
                        System.out.println(result);
                        System.out.println(contact.userId());
                        System.out.println("-------contact--------\n\r");
                    }
                }
            } else {
            }
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });


    }

    private Client checkClientInList(Client client) {
        if (clientList == null)
            clientList = new ArrayList<>();

        if (!clientList.contains(client))
            clientList.add(client);

        for (Client curClnt : clientList) {
            if (client.getId().equals(curClnt.getId())) {
                return curClnt;
            }
        }
        return client;
    }

    private Client changeProps(Client client) {
        if (!clientList.contains(client)) {
            clientList.add(client);
            return client;
        }

        clientList.remove(client);
        clientList.add(client);
        return client;
    }

    private void setPoints(Client clnt, int points) {
        for (Client curClnt : clientList) {
            if (clnt.getId().equals(curClnt.getId())) {
                curClnt.setPoints(points);
                clientList.remove(clnt);
                clientList.add(curClnt);
                break;
            }
        }
    }

    private static void printPassPortData(PassportData passportData) throws Exception {
        if (passportData == null)
            return;

        String privateKey = "abc_my_baby";
        EncryptedCredentials encryptedCredentials = passportData.credentials();
        Credentials credentials = encryptedCredentials.decrypt(privateKey);

        EncryptedPassportElement[] encryptedPassportElements = passportData.data();
        for (EncryptedPassportElement element : encryptedPassportElements) {
            DecryptedData decryptedData = element.decryptData(credentials);
            // DecryptedData can be cast to specific type by checking instanceOf
            if (decryptedData instanceof PersonalDetails) {
                PersonalDetails personalDetails = (PersonalDetails) decryptedData;

                Field[] fields = personalDetails.getClass().getFields();
                for (int i = 0; i < fields.length; i++) {
                    System.out.println("field: " + fields[i] + ":  " + personalDetails.getClass().getField(fields[i].getName()) + ":  " + personalDetails.getClass().getField(fields[i].getName()).toString());

                }

            }
            // Or by checking type of passport element
            if (element.type() == EncryptedPassportElement.Type.address) {
                ResidentialAddress address = (ResidentialAddress) decryptedData;
            }
        }
    }
}