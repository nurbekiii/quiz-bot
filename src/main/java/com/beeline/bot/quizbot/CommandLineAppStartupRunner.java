package com.beeline.bot.quizbot;

import com.beeline.bot.quizbot.entity.User;
import com.beeline.bot.quizbot.entity.*;
import com.beeline.bot.quizbot.service.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.passport.*;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author NIsaev on 27.09.2019
 */
@Component
public class CommandLineAppStartupRunner {
    private static final Logger LOG = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

    private List<User> userList;
    private Map<Long, User> usersCache;

    private String client_full_name = "client_full_name";
    private String ask_name_message_id = "ask_name_message_id";

    private String task1_message_id = "task1_message_id";
    private String task1_file_id = "task1_file_id";

    private String level1_message_id = "level1_message_id";
    private String level2_message_id = "level2_message_id";
    private String level3_message_id = "level3_message_id";


    private List<QuizText> textsList;
    private List<Task> tasksList;

    private String QUIZ_BTN1;
    private String QUIZ_BTN2;
    private String QUIZ_BTN3;
    private String QUIZ_BTN4;
    private String QUIZ_BTN5;
    private String QUIZ_BTN6;
    private String QUIZ_BTN7;
    private String QUIZ_BTN8;
    private String QUIZ_BTN9;


    @Autowired
    private UserService userService;

    @Autowired
    private PhotoSmileService photoSmileService;

    @Autowired
    private QuizTextService quizTextService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AnswerService answerService;


    @PostConstruct
    public void init() {
        usersCache = new HashMap<>();
        userList = new ArrayList<>();

        textsList = quizTextService.getAll();

        tasksList = taskService.getAll();
        tasksList.sort(Comparator.comparing(Task::getTitle).thenComparing(Comparator.comparing(Task::getOrder)));

        String[] res = getTextByCodes(new String[]{"quiz_btn1", "quiz_btn2", "quiz_btn3", "quiz_btn4", "quiz_btn5", "quiz_btn6", "quiz_btn7", "quiz_btn8", "quiz_btn9"}).toArray(new String[0]);
        //кнопки
        QUIZ_BTN1 = res[0];
        QUIZ_BTN2 = res[1];
        QUIZ_BTN3 = res[2];
        QUIZ_BTN4 = res[3];
        QUIZ_BTN5 = res[4];
        QUIZ_BTN6 = res[5];
        QUIZ_BTN7 = res[6];
        QUIZ_BTN8 = res[7];
        QUIZ_BTN9 = res[8];
    }

    @Value("${tlg.bot_token}")
    private String BOT_TOKEN;

    @Value("${tlg.local_temp.folder}")
    private String TEMP_FOLDER;

    private TelegramBot bot;


    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        bot = new TelegramBot(BOT_TOKEN);
        // Register for updates
        bot.setUpdatesListener(updates -> {
            if (updates != null) {
                for (Update update : updates) {
                    Message message = update.message();
                    if (message != null) {
                        String text = message.text();
                        long chatId = update.message().chat().id();
                        Chat chat = update.message().chat();

                        System.out.println("chat: " + chat);

                        User newUser = new User();
                        newUser.setTlgId(chatId);
                        newUser.setTlgUsername(chat.username());
                        newUser.setTlgFirstname(chat.firstName());
                        newUser.setTlgLastname(chat.lastName());

                        newUser = checkUserInList(newUser);
                        if (newUser.getId() == null) {
                            newUser = checkMe(newUser);
                            newUser = checkUserInList(newUser);
                        }

                        if (message.replyToMessage() != null) {
                            Integer repMessId = message.replyToMessage().messageId();

                            if (newUser.getTempAttr(client_full_name) == null && repMessId.equals(newUser.getTempAttr(ask_name_message_id))) {
                                //first name
                                newUser = setFirstName(chatId, newUser, message.text());
                            } else if (newUser.getTempAttr(client_full_name) != null && repMessId.equals(newUser.getTempAttr(task1_message_id))) {
                                //first task
                                PhotoSize[] sizes = message.photo();
                                newUser = sendFirstTask(chatId, newUser, sizes[0].fileId());
                            } else if (newUser.getTempAttr(client_full_name) != null && newUser.getTempAttr(task1_message_id) != null
                                    && (repMessId.equals(newUser.getTempAttr(level1_message_id)) ||
                                    repMessId.equals(newUser.getTempAttr(level2_message_id)) ||
                                    repMessId.equals(newUser.getTempAttr(level3_message_id))
                            )) {
                                processAnswers(chatId, newUser, message, repMessId);
                            }

                        }
                        if (text != null) {
                            switch (text) {
                                case "/start":
                                    newUser = startAction(chatId, newUser);
                                    break;

                                case "Фокус на клиента":
                                case "Лидерство и развитие команд":
                                case "Стратегическое мышление":
                                case "Открытость к экспериментам":
                                case "Бизнес-проницательность":
                                case "Нацеленность на результат":
                                case "Управление изменениями":
                                case "Честность и этика":
                                case "Сотрудничество":
                                    newUser = handleButtonTask(chatId, newUser, text, 1, level1_message_id);//FIFTH CHANGE
                                    break;
                            }
                        }
                        System.out.println("---------------\n\r");
                        System.out.println(chat);
                        System.out.println(text != null ? text : "");
                        System.out.println(message.messageId());
                        System.out.println("---------------\n\r");
                    }
                }
            }
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });


    }

    private User processAnswers(Long chatId, User newUser, Message message, Integer repMessId) {
        boolean isLev1 = repMessId.equals(newUser.getTempAttr(level1_message_id) != null ? newUser.getTempAttr(level1_message_id) : -1);
        boolean isLev2 = repMessId.equals(newUser.getTempAttr(level2_message_id) != null ? newUser.getTempAttr(level2_message_id) : -1);
        boolean isLev3 = repMessId.equals(newUser.getTempAttr(level3_message_id) != null ? newUser.getTempAttr(level3_message_id) : -1);

        //handle
        String answer = message.text();
        Document doc = message.document();
        PhotoSize sizes[] = message.photo();
        String fielId = (sizes != null ? sizes[0].fileId() : null);
        if (doc != null)
            fielId = doc.fileId();

        int nextLevel = (isLev1 && !isLev2 ? 2 : (isLev2 && !isLev3 ? 3 : (isLev3 ? 4 : 4)));

        Task task = null;
        for (Task tsk : newUser.getTasks()) {
            if (tsk.getOrder().intValue() == (nextLevel - 1))
                task = tsk;
        }

        Answer newAnswer = handleLevelAnswer(newUser, task, answer, fielId);
        if (nextLevel < 4)
            newUser = handleButtonTask(chatId, newUser, task.getTitle(), nextLevel, (nextLevel == 2 ? level2_message_id : level3_message_id));//SIXTH CHANGE
        else {
            //очистим кеш ответов
            newUser.setTempAttr(level1_message_id, null);
            newUser.setTempAttr(level1_message_id, null);
            newUser.setTempAttr(level1_message_id, null);

            String[] tsks = newUser.getTasks().stream().map(Task::getTitle).collect(Collectors.toSet()).toArray(new String[0]);

            String msg = getTextByCode("select_quiz"); //Выберите категорию
            sendQuizChoice(bot, chatId, msg, Arrays.asList(tsks));
        }

        return newUser;
    }

    private Answer handleLevelAnswer(User newUser, Task task, String textAnswer, String fileId) {
        java.io.File fl = null;
        byte[] bytes = null;
        try {
            if (fileId != null) {
                fl = downloadRemoteFile(fileId);
                bytes = java.nio.file.Files.readAllBytes(fl.toPath());
            }
        } catch (Exception t) {
            t.printStackTrace();
        }

        Integer userId = newUser.getId();
        String username = newUser.getUsername();
        String email = newUser.getEmail();
        int taskId = task.getId();
        int level = task.getOrder();
        String taskCategory = task.getTitle();

        Answer answer = new Answer();
        answer.setUserId(userId);
        answer.setOwner(username);
        answer.setEmail(email);
        answer.setComment("--------");

        answer.setTaskId(taskId);
        answer.setTextAnswer(textAnswer);
        answer.setTaskCategory(taskCategory);
        answer.setTaskLevel(level);
        answer.setTaskName(task.getDescription());
        if (fl != null) {
            answer.setAnswerFile(fl);
            answer.setFileContent(bytes);
        }

        Answer newAnswer = answerService.save(answer);
        return newAnswer;

    }

    private User startAction(long chatId, User newUser) {
        String msg = getTextByCode("welcome_text"); //"Добро пожаловать в игру Кубок компетенций или BeeHero! Have fun and become winner. Впереди тебя ждет много увлекательных приключений и новой информации, которые помогут тебе быть эффективными в твоей роли.";
        bot.execute(new SendMessage(chatId, msg));

        newUser.setPoints(100);
        String msg3 = getTextByCode("100_points"); //"Мы поздравляем тебя! Ты уже заработал свои первые 100 баллов. Чтобы ты мог продолжать зарабатывать баллы далее, предлагаю познакомиться";
        bot.execute(new SendMessage(chatId, msg3));

        String msg4 = getTextByCode("enter_your_name");//"Пожалуйста введите Ваши Имя и Фамилию";
        SendMessage send = new SendMessage(chatId, msg4);

        send.replyMarkup(new ForceReply());
        SendResponse response4 = bot.execute(send);
        int messId = response4.message().messageId();

        newUser.setTempAttr(ask_name_message_id, messId);
        newUser = changeProps(newUser); //FIRST CHANGE

        return newUser;
    }

    private User setFirstName(long chatId, User newUser, String clientName) {
        newUser.setTempAttr(client_full_name, clientName);
        newUser.setPoints(150);
        newUser = changeProps(newUser);  //SECOND CHANGE

        String msg22 = getTextByCode("50_points"); // "Поздравляем, ты заработал 50 баллов";
        SendResponse response22 = bot.execute(new SendMessage(chatId, msg22));

        String msg33 = getTextByCode("task1");  // "Для того чтобы ты мог понять суть игры, предлагаем тебе пройти первое задание. Пройди по ссылке и пройди онлайн тренинг на нашей корпоративной платформе Crossknowledge и ознакомься с ним. Для подтверждения выполнения задания загрузи скрин со 100% прогрессом по курсу.";
        SendMessage send33 = new SendMessage(chatId, msg33);

        ////????????????
        send33.replyMarkup(new ForceReply());
        SendResponse response33 = bot.execute(send33);
        int messId = response33.message().messageId();

        newUser.setTempAttr(task1_message_id, messId);
        newUser = changeProps(newUser); //THIRD CHANGE

        return newUser;
    }

    private java.io.File downloadRemoteFile(String fileId) throws Exception {
        GetFile request = new GetFile(fileId);
        GetFileResponse getFileResponse = bot.execute(request);

        com.pengrad.telegrambot.model.File file = getFileResponse.file();

        byte[] bytes = bot.getFileContent(file);
        String name = file.filePath().substring(file.filePath().indexOf("/") + 1);
        return Files.write(Paths.get(TEMP_FOLDER + name), bytes).toFile();
    }

    private User sendFirstTask(long chatId, User newUser, String fileId) {

        newUser.setTempAttr(task1_file_id, fileId);
        newUser.setPoints(newUser.getPoints() + 200);
        newUser = changeProps(newUser); //FOURTH CHANGE

        PhotoSmile photo = new PhotoSmile();
        photo.setUsername(newUser.getUsername());
        photo.setOwner(newUser.getEmail());
        photo.setDesc("first task complete screenshot");

        try {
            java.io.File file2 = downloadRemoteFile(fileId);
            byte[] bytes = java.nio.file.Files.readAllBytes(file2.toPath());
            photo.setImageContent(bytes);
            photo.setImageFile(file2);
        } catch (Exception t) {
            t.printStackTrace();
        }

        PhotoSmile smile = photoSmileService.save(photo);

        String msg = getTextByCode("200_points"); //"Поздравляем! +200 баллов к твоему счету баллов. Для того чтобы продолжить игру, выбери компетенцию";
        sendQuizChoice(bot, chatId, msg, null);

        return newUser;
    }

    private void sendQuizChoice(TelegramBot bot, long chatId, String msg, List<String> excludeBtns) {
        String[][] res = new String[][]{
                new String[]{QUIZ_BTN1, QUIZ_BTN2, QUIZ_BTN3},
                new String[]{QUIZ_BTN4, QUIZ_BTN5, QUIZ_BTN6},
                new String[]{QUIZ_BTN7, QUIZ_BTN8, QUIZ_BTN9}
        };
        if (excludeBtns != null) {
            for (String btnText : excludeBtns) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (res[i][j] !=null && res[i][j].equals(btnText)) {
                            res[i][j] = null;
                        }
                    }
                }
            }

            res = new String[][]{
                    Arrays.asList(res[0]).stream().filter(line -> line != null).collect(Collectors.toList()).toArray(new String[0]),
                    Arrays.asList(res[1]).stream().filter(line -> line != null).collect(Collectors.toList()).toArray(new String[0]),
                    Arrays.asList(res[2]).stream().filter(line -> line != null).collect(Collectors.toList()).toArray(new String[0])
            };
        }

        SendMessage send = new SendMessage(chatId, msg);
        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(res)
                //.oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
        SendResponse response = bot.execute(send.replyMarkup(replyKeyboardMarkup));
        //int messId = response.message().messageId();
    }

    private User handleButtonTask(Long chatId, User user, String title, int level, String level_message_id) {
        Task task = getTaskByTitleLevel(title, level);
        String description = task.getDescription();
        SendMessage send = new SendMessage(chatId, description);

        send.replyMarkup(new ForceReply());
        SendResponse response9 = bot.execute(send);
        int messId = response9.message().messageId();

        user.setTempAttr("title", title);
        user.setTempAttr(level_message_id, messId);
        user.addTask(task);
        //user = changeProps(user); //FIFTH CHANGE
        return user;
    }

    private Task getTaskByTitleLevel(String title, int level) {
        for (Task task : tasksList) {
            if (task.getTitle().equals(title) && task.getOrder().intValue() == level)
                return task;
        }
        return null;
    }

    private Task getTaskByCode(String code) {
        for (Task task : tasksList) {
            if (task.getCode().equals(code))
                return task;
        }
        return null;
    }

    private String getTextByCode(String code) {
        for (QuizText text : textsList) {
            if (text.getCode().equals(code))
                return text.getText();
        }
        return null;
    }

    private List<String> getTextByCodes(String[] codes) {
        List<String> list = new ArrayList<>();
        for (String code : codes) {
            for (QuizText text : textsList) {
                if (text.getCode().equals(code))
                    list.add(text.getText());
            }
        }
        return list;
    }

    private User checkMe(User user) {
        System.out.println("checkMe STARTED");
        List<User> list = userService.getAllUsers();
        if (list != null && list.size() > 0) {
            for (User usr : list) {
                if (usr.getTlgId() != null && usr.getTlgId().equals(user.getTlgId())) {
                    //возвратим что есть
                    if (usr.getTlgUsername() != null) {
                        LOG.info("found user details");
                        return usr;
                    }

                    //обновим в БД
                    usr.setTlgUsername(user.getTlgUsername());
                    usr.setTlgFirstname(user.getTlgFirstname());
                    usr.setTlgLastname(user.getTlgLastname());
                    usr.setPassword("12345");
                    usr = userService.update(usr);
                    LOG.info("found user details");
                    return usr;
                }
            }
        }

        //создадим в БД Новую
        User usr = new User();
        usr.setUsername(user.getTlgUsername());
        usr.setEmail(user.getTlgUsername() + "@mail.xxx");
        usr.setPassword("12345");

        usr.setTlgUsername(user.getTlgUsername());
        usr.setTlgFirstname(user.getTlgFirstname());
        usr.setTlgLastname(user.getTlgLastname());

        System.out.println("checkMe ENDED");
        return userService.save(usr);
    }

    private User checkUserInList(User user) {
        for (User curUsr : userList) {
            if (user.getId() == null) {
                if (curUsr.getId() != null && curUsr.getTlgId().equals(user.getTlgId())) {
                    return curUsr;
                }
            } else {
                if (user.getId() != null && curUsr.getTlgId().equals(user.getTlgId())) {
                    if (curUsr.getId() == null) {
                        userList.remove(curUsr);
                        userList.add(user);
                        return user;
                    }
                    return curUsr;
                }
            }
        }

        for (User curUsr : userList) {
            if (user.getTlgId().equals(curUsr.getTlgId())) {
                return curUsr;
            }
        }

        if (!userList.contains(user))
            userList.add(user);


        return user;
    }

    private User findUserInDB(User usr) {
        List<User> list = userService.getAllUsers();
        if (list != null && !list.isEmpty()) {
            for (User curUser : list) {
                if (curUser.getTlgId() != null && curUser.getTlgId().equals(usr.getTlgId())) {
                    return curUser;
                }

                if (curUser.getTlgUsername() != null && curUser.getTlgUsername().equals(usr.getTlgUsername())) {
                    return curUser;
                }
            }
        }
        return null;
    }

    private User changeProps(User user) {

        //поищем в БД
        User dbUser = findUserInDB(user);
        if (dbUser != null) {
            dbUser.setPoints(user.getPoints());
            dbUser.setTlgId(user.getTlgId());
            dbUser.setTlgUsername(user.getTlgUsername());

            dbUser.setTlgFirstname(user.getTlgFirstname());
            dbUser.setTlgLastname(user.getTlgLastname());

            if (user.getTempAttr() != null && !user.getTempAttr().isEmpty())
                dbUser.setTempAttr(user.getTempAttr());

            //ФИО
            if (user.getTempAttr(client_full_name) != null) {
                dbUser.setTlgFullname(user.getTempAttr(client_full_name).toString());
            }
        } else {
            if (user.getTempAttr(client_full_name) != null)
                user.setTlgFullname(user.getTempAttr(client_full_name).toString());

            dbUser = user;
        }

        dbUser.setPassword("12345"); //TO-DO ???

        userList.remove(dbUser); //удалим старый
        userList.add(dbUser); //обновим новым

        dbUser = userService.save(dbUser); //сохраним в БД
        ///
        dbUser.setTempAttr(user.getTempAttr());
        return dbUser;
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
