package com.beeline.bot.quizbot.bot;

import com.beeline.bot.quizbot.entity.Client;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.passport.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.File;
import java.lang.reflect.Field;

/**
 * @author NIsaev on 18.09.2019
 */
public class Main3 {

    public static void main(String[] args) {
        //create a new Telegram bot object to start talking with Telegram
        String BOT_TOKEN = "940004167:AAH7DbyZ6H4bJDrp6jx8aMdpWcVXlg804nM";
        //TelegramBot bot = new TelegramBot("940004167:AAH7DbyZ6H4bJDrp6jx8aMdpWcVXlg804nM"); //new TelegramBot.Builder("940004167:AAH7DbyZ6H4bJDrp6jx8aMdpWcVXlg804nM").okHttpClient(client).build();
        final long CHAT_ID = 508363280;

        //OkHttpClient client = new OkHttpClient();
        //TelegramBot bot = new TelegramBot.Builder(BOT_TOKEN).okHttpClient(client).build();
        TelegramBot bot = new TelegramBot(BOT_TOKEN);

        //bot.execute(new SendMessage(CHAT_ID, "Hello, My friends! What do you want? "));

        // Register for updates
        bot.setUpdatesListener(updates -> {
            if (updates != null) {
                for (Update update : updates) {
                    Message message = update.message();
                    if (message != null) {
                        String text = message.text();
                        if (text == null)
                            continue;

                        long chatId = update.message().chat().id();
                        Chat chat = update.message().chat();
                        String fullName = (chat.firstName() != null ? chat.firstName() : "") + (chat.lastName() != null ? " " + chat.lastName() : "");
                        String usr = (chat.username() != null ? chat.username() : fullName);

                        PassportData passportData = update.message().passportData();
                        //InlineQuery inlineQuery = update.inlineQuery();

                        Client clnt = new Client(chatId, chat.firstName(), chat.lastName(), chat.username());

                        switch (text) {
                            case "/weather":
                                SendResponse response2 = bot.execute(new SendMessage(chatId, "Hello " + fullName + ". weather is ok i think"));
                                int messageId = response2.message().messageId();

                                try {
                                    Thread.sleep(5000);
                                } catch (Exception t) {
                                }
                                //Editing text
                                EditMessageText editMessageText = new EditMessageText(chatId, messageId, "Sorry " + fullName + ". Weather can be not OK")
                                        .parseMode(ParseMode.HTML)
                                        .disableWebPagePreview(true)
                                        .replyMarkup(new InlineKeyboardMarkup());

                                BaseResponse response4 = bot.execute(editMessageText);
                                break;

                            case "/balance":
                                bot.execute(new SendMessage(chatId, "@" + usr + " balance is nearly 50 soms"));
                                break;
                            case "/smile":
                                bot.execute(new SendMessage(chatId, fullName + " please smile. You are smart guy"));
                                break;

                            /*case "/buttons":
                                SendResponse response55 = bot.execute(new SendMessage(chatId, "Finish Quiz?").replyMarkup(new ReplyKeyboardMarkup(new String[]{"Yes"}, new String[]{"NO"})));
                                String result = response55.message().text();
                                System.out.println("Button result: " + result);
                                break;*/

                            case "/cry":
                                SendResponse response5 = bot.execute(new SendMessage(chatId, fullName + " do not cry. Are you ok?"));
                                int messageId2 = response5.message().messageId();
                                try {
                                    Thread.sleep(5000);
                                } catch (Exception t) {
                                }
                                DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId2);
                                BaseResponse response6 = bot.execute(deleteMessage);
                                break;

                            case "/gift":
                                // File or byte[] or string fileId of existing sticker or string URL
                                SendSticker sendSticker = new SendSticker(chatId, new File("C:/DANNYE/sticker.png"));
                                SendResponse response = bot.execute(sendSticker);
                                int i = 0;
                                break;

                            case "/invoice":
                                SendInvoice sendInvoice = new SendInvoice((int) chatId,
                                        "title", "desc", "my_payload", "providerToken", "my_start_param", "USD",
                                        new LabeledPrice("label", 200))
                                        .needPhoneNumber(true)
                                        .needShippingAddress(true)
                                        .isFlexible(true)
                                        .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton[]{
                                                new InlineKeyboardButton("just pay").pay(),
                                                new InlineKeyboardButton("google it").url("www.google.com")
                                        }));
                                SendResponse response7 = bot.execute(sendInvoice);
                                int k = 0;
                                break;

                            case "/photo":
                                Keyboard keyboard = new ReplyKeyboardMarkup(
                                        new KeyboardButton[]{
                                                new KeyboardButton("text"),
                                                new KeyboardButton("contact").requestContact(true),
                                                new KeyboardButton("location").requestLocation(true)
                                        }
                                );
                                break;

                            case "/location":
                                SendLocation loc = new SendLocation(chatId, (float) 42.872537, (float) 74.6177846);
                                bot.execute(loc);
                                break;

                            case "/passport":
                                try {
                                    printPassPortData(passportData);
                                } catch (Exception t) {
                                    t.printStackTrace();
                                }
                                break;
                        }
                        System.out.println("---------------\n\r");
                        System.out.println(message.text());
                        System.out.println(message.chat());
                        System.out.println(message.messageId());
                        System.out.println("---------------\n\r");
                    }

                }
            } else {
            }
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });


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