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
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.vdurmont.emoji.EmojiParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author NIsaev on 27.09.2019
 */
@Component
public class CommandLineAppStartupRunner {
    private static final Logger LOG = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

    private Map<Integer, User> usersCache;

    private String client_full_name = "client_full_name";
    private String ask_name_message_id = "ask_name_message_id";

    private String input_comment_message_id = "input_comment_message_id";
    private String comment_message = "comment_message";

    private String task1_message_id = "task1_message_id";
    private String task1_file_id = "task1_file_id";

    private String level1_message_id = "level1_message_id";
    private String level2_message_id = "level2_message_id";
    private String level3_message_id = "level3_message_id";

    private final String FIRST_TASK = "FIRST_TASK";
    private String titleEx = "title";
    private String CUR_LEVEL = "CUR_LEVEL";

    private final String helpBtn = ":wrench: Help";
    private final String commentBtn = ":memo: Комментарий"; //comment_btn
    private final String competBtn = ":shield: Компетенции";
    private final String myResultBtn = ":bank: Мой результат";

    private final String BTN_TASK1 = "Задание 1";
    private final String BTN_TASK2 = "Задание 2";
    private final String BTN_TASK3 = "Задание 3";

    private final String BTN_ANSWER = ":point_up: Ответить";
    private final String BTN_BACK = ":point_left: Назад";

    private List<Pattern> buttonsPattern;

    private List<QuizText> textsList;
    private List<Task> tasksList;
    private List<Category> categoriesList;

    private String QUIZ_BTN1;
    private String QUIZ_BTN2;
    private String QUIZ_BTN3;
    private String QUIZ_BTN4;
    private String QUIZ_BTN5;
    private String QUIZ_BTN6;
    private String QUIZ_BTN7;
    private String QUIZ_BTN8;
    private String QUIZ_BTN9;

    private List<String> quizButtonsList;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCommentService userCommentService;

    @Autowired
    private QuizTextService quizTextService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AnswerService answerService;

    @Value("${tlg.bot_token}")
    private String BOT_TOKEN;

    @Value("${tlg.local_temp.folder}")
    private String TEMP_FOLDER;

    @Value("${rest.url_uploads}")
    private String urlUploads;

    private TelegramBot bot;

    @PostConstruct
    public void init() {
        usersCache = new HashMap<>();
        //userList = new ArrayList<>();

        textsList = quizTextService.getAll();
        categoriesList = categoryService.getAll();

        tasksList = taskService.getAll();
        tasksList.sort(Comparator.comparing(Task::getTitle).thenComparing(Comparator.comparing(Task::getId)));

        String[] res = getTextByCodes(new String[]{"quiz_btn1", "quiz_btn2", "quiz_btn3", "quiz_btn4", "quiz_btn5", "quiz_btn6", "quiz_btn7", "quiz_btn8", "quiz_btn9"}).toArray(new String[0]);
        //кнопки
        QUIZ_BTN1 = EmojiParser.parseToUnicode(getCategoryById(1).getCode() + " " + getCategoryById(1).getTitle()); //res[0];
        QUIZ_BTN2 = EmojiParser.parseToUnicode(getCategoryById(2).getCode() + " " + getCategoryById(2).getTitle()); //res[1];
        QUIZ_BTN3 = EmojiParser.parseToUnicode(getCategoryById(3).getCode() + " " + getCategoryById(3).getTitle()); //res[2];
        QUIZ_BTN4 = EmojiParser.parseToUnicode(getCategoryById(4).getCode() + " " + getCategoryById(4).getTitle());
        QUIZ_BTN5 = EmojiParser.parseToUnicode(getCategoryById(5).getCode() + " " + getCategoryById(5).getTitle());
        QUIZ_BTN6 = EmojiParser.parseToUnicode(getCategoryById(6).getCode() + " " + getCategoryById(6).getTitle());
        QUIZ_BTN7 = EmojiParser.parseToUnicode(getCategoryById(7).getCode() + " " + getCategoryById(7).getTitle());
        QUIZ_BTN8 = EmojiParser.parseToUnicode(getCategoryById(8).getCode() + " " + getCategoryById(8).getTitle());
        QUIZ_BTN9 = EmojiParser.parseToUnicode(getCategoryById(9).getCode() + " " + getCategoryById(9).getTitle());

        quizButtonsList = new ArrayList<>();
        quizButtonsList.addAll(Arrays.asList(new String[]{QUIZ_BTN1, QUIZ_BTN2, QUIZ_BTN3, QUIZ_BTN4, QUIZ_BTN5, QUIZ_BTN6, QUIZ_BTN7, QUIZ_BTN8, QUIZ_BTN9}));

        //for buttons
        buttonsPattern = new ArrayList<>();

        for (Category cat : categoriesList) {
            Pattern pattern = Pattern.compile(EmojiParser.parseToUnicode(cat.getCode() + " " + cat.getTitle()) + " (\\(\\d{2}|d{3})\\%\\)");
            buttonsPattern.add(pattern);
        }

    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        bot = new TelegramBot(BOT_TOKEN);
        // Register for updates
        bot.setUpdatesListener(updates -> {
            try {
                if (updates != null) {
                    for (Update update : updates) {
                        Message message = update.message();
                        if (message != null) {
                            String text = message.text();
                            long chatId = update.message().chat().id();
                            Chat chat = update.message().chat();

                            User newUser = new User();
                            newUser.setTlgId(chatId);
                            newUser.setTlgUsername(chat.username());
                            newUser.setTlgFirstname(chat.firstName());
                            newUser.setTlgLastname(chat.lastName());

                            newUser = checkUserInListEx(newUser);
                            if (newUser.getId() == null) {
                                newUser = checkMeEx(newUser);
                                newUser = checkUserInListEx(newUser);
                            }

                            //ответы на вопросы
                            if (message.replyToMessage() != null) {
                                Integer repMessId = message.replyToMessage().messageId();

                                if (newUser.getTempAttr(client_full_name) == null && repMessId.equals(newUser.getTempAttr(ask_name_message_id))) {
                                    //first name
                                    newUser = setFirstName(chatId, newUser, message.text());
                                } else if (/*newUser.getTempAttr(client_full_name) != null &&*/ repMessId.equals(newUser.getTempAttr(task1_message_id))) {
                                    //error in first task
                                    if (message.photo() == null && (message.text() != null || message.document() != null)) {
                                        String msg = getTextByCode("answer_format_error");
                                        sendEmojiText(chatId, msg + ":scream:");
                                        newUser = firstTaskRequest(chatId, newUser);
                                    } else {
                                        //first task
                                        PhotoSize[] sizes = message.photo();
                                        newUser = sendFirstTask(chatId, newUser, sizes[0].fileId());
                                    }
                                } else if (repMessId.equals(newUser.getTempAttr(input_comment_message_id))) {
                                    //comment save
                                    setComment(chatId, newUser, message.text());
                                    newUser = senQuizSelect(chatId, newUser);
                                } else if ((repMessId.equals(newUser.getTempAttr(level1_message_id)) ||
                                        repMessId.equals(newUser.getTempAttr(level2_message_id)) ||
                                        repMessId.equals(newUser.getTempAttr(level3_message_id)))) {
                                    User usr = processAnswers(chatId, newUser, message, repMessId);
                                    if (usr == null) {
                                        newUser = askInputAnswer(chatId, newUser);
                                    }
                                }
                            }

                            //кнопки
                            if (text != null) {
                                switch (text) {
                                    case "/start":
                                        newUser = startAction(chatId, newUser);
                                        break;
                                }

                                //компетенции
                                if (text.contains("[") && text.contains("]")) {
                                    String text2 = text.substring(0, text.indexOf("[") - 1);
                                    if (quizButtonsList.contains(text2)) {
                                        newUser = drawTaskButtons(chatId, newUser, text2);
                                    }
                                }

                                if (quizButtonsList.contains(text)) {
                                    newUser = drawTaskButtons(chatId, newUser, text);
                                }

                                //кнопки
                                String parsedEmoji = getBackFromEmoji(text);

                                switch (parsedEmoji) {
                                    case helpBtn:
                                        showHelpMessage(chatId);
                                        break;

                                    case myResultBtn:
                                        showMyResult(chatId, newUser);
                                        break;

                                    case competBtn:
                                        newUser = senQuizSelect(chatId, newUser);
                                        break;

                                    case commentBtn:
                                        newUser = inputCommentRequest(chatId, newUser);
                                        break;

                                    case BTN_BACK:
                                        if (newUser.getTempAttr(titleEx) != null)
                                            newUser = drawTaskButtons(chatId, newUser, newUser.getTempAttr(titleEx).toString());
                                        break;

                                    case BTN_ANSWER:
                                        newUser = askInputAnswer(chatId, newUser);
                                        break;

                                    //default:  botMissUndestand(chatId);   break;
                                }

                                String clearedText = getClearText(text, true);
                                if (newUser != null && newUser.getTempAttr(titleEx) != null) {
                                    switch (clearedText) {
                                        case BTN_TASK1:
                                            newUser = handleButtonTaskNew(chatId, newUser, newUser.getTempAttr(titleEx).toString(), 1);//FIFTH CHANGE
                                            break;
                                        case BTN_TASK2:
                                            newUser = handleButtonTaskNew(chatId, newUser, newUser.getTempAttr(titleEx).toString(), 2);//FIFTH CHANGE
                                            break;
                                        case BTN_TASK3:
                                            newUser = handleButtonTaskNew(chatId, newUser, newUser.getTempAttr(titleEx).toString(), 3);//FIFTH CHANGE
                                            break;
                                    }
                                }
                            }
                            LOG.info("---------------");
                            LOG.info(chat.toString());
                            LOG.info(text != null ? text : "");
                            LOG.info(message.messageId().toString());
                            LOG.info("---------------");
                        }
                    }
                }
                // return id of last processed update or confirm them all
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            } catch (Exception t) {
                LOG.error(t.toString());
                t.printStackTrace();
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private User drawTaskButtons(Long chatId, User newUser, String title) {
        String title1 = getClearText(title, true);
        List<Task> tasks = newUser.getTasks().stream().filter(tsk -> tsk.getTitle().equals(title1) && tsk.isFinished()).collect(Collectors.toList());

        int resFin[] = new int[]{0, 0, 0};
        for (int i = 1; i < 4; i++) {
            int lev = i;
            int num = (int) tasks.stream().filter(tsk -> tsk.getTitle().equals(title1) && tsk.isFinished() && tsk.getOrder().equals(lev)).count();
            resFin[i - 1] = num;
        }

        String str1 = ":green_heart: ", str2 = ":yellow_heart: ";
        String[][] res = new String[][]{new String[]{
                (EmojiParser.parseToUnicode((resFin[0] > 0 ? str1 : str2) + BTN_TASK1)),
                (resFin[0] > 0 ? EmojiParser.parseToUnicode((resFin[1] > 0 ? str1 : str2) + BTN_TASK2) : null),
                (resFin[0] > 0 && resFin[1] > 0 ? EmojiParser.parseToUnicode((resFin[2] > 0 ? str1 : str2) + BTN_TASK3) : null),
                (EmojiParser.parseToUnicode(competBtn))
        }};

        res = new String[][]{
                Arrays.asList(res[0]).stream().filter(line -> line != null).collect(Collectors.toList()).toArray(new String[0]),
        };


        String msg = getTextByCode("compet_tasks");
        showButtons(chatId, msg + " " + title1 + " :point_down:", res);

        newUser.setTempAttr(titleEx, title1);
        return newUser;
    }

    private String getBackFromEmoji(String input) {
        return EmojiParser.parseFromUnicode(input, e -> ":" + e.getEmoji().getAliases().get(0) + ":");
    }

    private boolean hasUserData(User newUser) {
        return (newUser != null && newUser.getTlgFullname() != null && newUser.getPoints() != null);
    }

    private User senQuizSelect(Long chatId, User newUser) {
        if (!hasUserData(newUser)) {
            return startAction(chatId, newUser);
        }

        String msg = getTextByCode("select_quiz"); //Выберите компетенцию
        return sendQuizChoice(chatId, newUser, msg);
    }

    private void sendCongratText(long chatId, boolean hasDouble) {
        String text = getTextByCode("100_points_easy");
        if (hasDouble) {
            text = getTextByCode("200_points_easy");
        }
        sendEmojiText(chatId, ":gift: " + text);
    }

    private User processAnswers(Long chatId, User newUser, Message message, Integer repMessId) {
        //handle
        String answer = message.text();
        Document doc = message.document();
        Audio audio = message.audio();
        Video video = message.video();
        Voice voice = message.voice();
        Sticker sticker = message.sticker();
        Animation animation = message.animation();
        PhotoSize sizes[] = message.photo();

        Task task = (newUser.getTasks().get(newUser.getTasks().size() - 1));

        boolean hasText = false, hasDoc = false, hasPdf = false, hasPhoto = false;
        String resultTypes = task.getResult().toLowerCase();
        if (answer != null) {
            hasText = resultTypes.contains("text") || (resultTypes.contains("link") && (answer.startsWith("http://") || answer.startsWith("https://")));
        }

        if (doc != null) {
            hasDoc = resultTypes.contains("file");
        }
        if (doc != null) {
            hasPdf = resultTypes.contains("pdf") && doc.fileName().endsWith(".pdf");
        }

        if (audio != null || voice != null) {
            hasDoc = resultTypes.contains("voice message");
        }

        if (video != null || animation != null) {
            hasDoc = resultTypes.contains("video");
        }

        if (sizes != null || sticker != null) {
            hasPhoto = resultTypes.contains("picture") || resultTypes.contains("sticker");
        }

        //Неправильный формат ответа
        if (!(hasText || hasDoc || hasPhoto || hasPdf)) {
            String msg = getTextByCode("answer_format_error_ex"); //Неправильный формат ответа
            sendEmojiText(chatId, ":scream_cat: " + msg + ":  " + resultTypes + " :pushpin:");
            return null;
        }

        boolean isLev1 = repMessId.equals(newUser.getTempAttr(level1_message_id) != null ? newUser.getTempAttr(level1_message_id) : -1);
        boolean isLev2 = repMessId.equals(newUser.getTempAttr(level2_message_id) != null ? newUser.getTempAttr(level2_message_id) : -1);
        boolean isLev3 = repMessId.equals(newUser.getTempAttr(level3_message_id) != null ? newUser.getTempAttr(level3_message_id) : -1);

        String fileId = (sizes != null ? sizes[0].fileId() : null);
        String fileName = "";
        int size = 0;
        if (doc != null) {
            fileId = doc.fileId();
            size = doc.fileSize();
            fileName = doc.fileName();
        } else if (audio != null) {
            fileId = audio.fileId();
            size = audio.fileSize();
            fileName = audio.title();
        } else if (voice != null) {
            fileId = voice.fileId();
            size = voice.fileSize();
            //fileName = doc.fileName(); ///?????
        } else if (video != null) {
            fileId = video.fileId();
            size = video.fileSize();
            //fileName = doc.fileName(); //?????
        } else if (animation != null) {
            fileId = animation.fileId();
            size = animation.fileSize();
            fileName = animation.fileName();
        } else if (sticker != null) {
            fileId = sticker.fileId();
            size = sticker.fileSize();
            fileName = sticker.setName();
        }
        int sizes1 = (int) (size / 1024);
        if (sizes1 > 100000) {
            String msg = getTextByCode("size_limit"); //Вы отправили файл большого размера.
            sendEmojiText(chatId, ":scream_cat: " + msg + ":  " + resultTypes + " :blue_book:");
            return null;
        }

        int nextLevel = (isLev1 && !isLev2 ? 2 : (isLev2 && !isLev3 ? 3 : (isLev3 ? 4 : 4)));

        Task task2 = null;
        if (task != null) {
            int num = (int) tasksList.stream().filter(tsk -> tsk.getSameTaskId() != null && tsk.getSameTaskId().equals(task.getId())).count();
            if (num > 0)
                task2 = tasksList.stream().filter(tsk -> tsk.getSameTaskId() != null && tsk.getSameTaskId().equals(task.getId())).collect(Collectors.toList()).get(0);
        }

        //если такое задание уже было ранее выполнено
        if (!newUser.getTasks().isEmpty() && task2 != null) {
            int taskId = task2.getId().intValue();
            int num = (int) newUser.getTasks().stream().filter(tsk -> tsk.isFinished() && tsk.getId().intValue() == taskId).count();
            if (num > 0)
                task2 = null;
        }

        //запись ответа в БД
        Answer newAnswer = handleLevelAnswer(newUser, task, task2, answer, fileId, null, fileName);
        if (nextLevel == 2) {
            //Поздравляем, ты заработал(-ла) 100 баллов! Ты можешь оставаться в этой компетенции или выбрать другую.
            boolean hasDuplicateTask = (task2 != null);
            sendCongratText(chatId, hasDuplicateTask);
        } else {
            String msg = getTextByCode("answer_received");
            sendEmojiText(chatId, msg + " :gift:");
        }

        ///task finished
        Integer points = (task.getPoint() != null ? task.getPoint() : 0) + (task2 != null && task2.getPoint() != null ? task2.getPoint() : 0);
        newUser.setPoints(newUser.getPoints() + points); //SEVENTH CHANGE

        task.setFinished(true);
        newUser.addTask(task);
        if (task2 != null) {
            task2.setFinished(true);
            newUser.addTask(task2);
        }
        newUser = changeProps(newUser);

        newUser = senQuizSelect(chatId, newUser);
        if (nextLevel >= 4) {
            //очистим кеш ответов
            newUser.setTempAttr(level1_message_id, null);
            newUser.setTempAttr(level2_message_id, null);
            newUser.setTempAttr(level3_message_id, null);
            newUser = changeProps(newUser);
        }

        return newUser;
    }

    private Answer handleLevelAnswer(User newUser, Task task, Task task2, String textAnswer, String fileId, String comment, String fileName) {
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

        Answer newAnswer = saveAnswer(newUser, task, fileId, fl, bytes, textAnswer, comment, fileName);

        if (task2 != null) {
            newAnswer = saveAnswer(newUser, task2, fileId, fl, bytes, textAnswer, comment, fileName);
        }

        return newAnswer;
    }

    private List<Task> getFinishedTasks(int userId) {
        TaskFilter filter = new TaskFilter(userId, null, null);
        List<Integer> taskIds = answerService.getAnswersByFilter(filter).stream().map(Answer::getTaskId).collect(Collectors.toList());
        if (taskIds != null && !taskIds.isEmpty()) {
            List<Task> taskList = taskService.getTaskByIds(taskIds);
            if (taskList != null && !taskList.isEmpty())
                taskList.stream().forEach(tsk -> tsk.setFinished(true)); //mark finished

            return taskList;
        }
        return null;
    }

    private User startAction(long chatId, User newUser) {
        //есть ФИО, есть первое задание
        if (newUser.getId() != null && newUser.getTlgFullname() != null) {
            TaskFilter filter = new TaskFilter(newUser.getId(), 1, FIRST_TASK);
            int num = answerService.getAnswersByFilter(filter).size();
            if (num > 0) {
                String msg = getTextByCode("hello_text"); //Привет
                sendEmojiText(chatId, msg + " " + newUser.getTlgFullname() + "! :heart: :green_heart: :purple_heart:");

                msg = getTextByCode("press_for_task"); //Для заданий нажмите на "Компетенции"

                String res[][] = new String[][]{new String[]{EmojiParser.parseToUnicode(competBtn), EmojiParser.parseToUnicode(commentBtn), EmojiParser.parseToUnicode(myResultBtn), EmojiParser.parseToUnicode(helpBtn)}};
                showButtons(chatId, (msg + " :point_down:"), res);
                return newUser;
            } else {
                return firstTaskRequest(chatId, newUser);
            }
        }

        String msg = getTextByCode("welcome_text"); //"Добро пожаловать в игру Кубок компетенций или BeeHero! Have fun and become winner. Впереди тебя ждет много увлекательных приключений и новой информации, которые помогут тебе быть эффективными в твоей роли.";
        sendEmojiText(chatId, msg + ":clap::clap::clap:");

        newUser.setPoints(100);
        String msg3 = getTextByCode("100_points"); //"Мы поздравляем тебя! Ты уже заработал свои первые 100 баллов. Чтобы ты мог продолжать зарабатывать баллы далее, предлагаю познакомиться";
        sendEmojiText(chatId, msg3 + ":moneybag::moneybag::moneybag:");

        String msg4 = getTextByCode("enter_your_name");//"Пожалуйста введите Ваши Имя и Фамилию";
        int messId = sendEmojiForceReply(chatId, msg4 + ":keyboard:");

        newUser.setTempAttr(ask_name_message_id, messId);
        newUser = changeProps(newUser); //FIRST CHANGE

        return newUser;
    }

    private User setFirstName(long chatId, User newUser, String clientName) {
        newUser.setTempAttr(client_full_name, clientName);
        if (newUser.getPoints() == null || newUser.getPoints().intValue() <= 150)
            newUser.setPoints(150);
        newUser.setTlgFullname(clientName);
        newUser = changeProps(newUser);  //SECOND CHANGE

        String msg = getTextByCode("50_points"); // "Поздравляем, ты заработал 50 баллов";
        sendEmojiText(chatId, msg + ":moneybag::moneybag:");

        newUser = firstTaskRequest(chatId, newUser);

        return newUser;
    }

    private void sendEmojiText(long chatId, String msg) {
        bot.execute(new SendMessage(chatId, EmojiParser.parseToUnicode(msg)));
    }

    private User firstTaskRequest(long chatId, User newUser) {
        Task task = getTaskByTitleLevel(FIRST_TASK, 1);
        int messId = sendEmojiForceReply(chatId, ":game_die: :bowling:" + task.getDescription());

        newUser.addTask(task);
        newUser.setTempAttr(task1_message_id, messId);
        newUser = changeProps(newUser); //THIRD CHANGE

        return newUser;
    }

    private User setComment(long chatId, User newUser, String comment) {
        newUser.setTempAttr(comment_message, comment);
        newUser = changeProps(newUser);  //EIGHT' CHANGE

        String msg = getTextByCode("comment_received"); // "Ваш комментарий принят";
        sendEmojiText(chatId, msg + " :yellow_heart:");

        UserComment userComment = new UserComment(null, newUser.getEmail(), newUser.getUsername(), comment);
        userCommentService.save(userComment);

        return newUser;
    }

    private java.io.File downloadRemoteFile(String fileId) throws Exception {
        GetFile request = new GetFile(fileId);
        GetFileResponse getFileResponse = bot.execute(request);

        com.pengrad.telegrambot.model.File file = getFileResponse.file();

        //file.fileSize() ///?????????????????

        byte[] bytes = bot.getFileContent(file);
        String name = file.filePath().substring(file.filePath().indexOf("/") + 1);
        return Files.write(Paths.get(TEMP_FOLDER + name), bytes).toFile();
    }

    private User sendFirstTask(long chatId, User newUser, String fileId) {
        Task task = newUser.getTasks().stream().filter(tsk -> tsk.getCode().equals(FIRST_TASK)).collect(Collectors.toList()).get(0);

        int points = task.getPoint(); //200 points
        newUser.setTempAttr(task1_file_id, fileId);
        newUser.setPoints(newUser.getPoints() + points);
        newUser = changeProps(newUser); //FOURTH CHANGE

        java.io.File file = null;
        byte[] bytes = null;
        try {
            file = downloadRemoteFile(fileId);
            bytes = java.nio.file.Files.readAllBytes(file.toPath());
        } catch (Exception t) {
            t.printStackTrace();
        }

        Answer newAnswer = saveAnswer(newUser, task, fileId, file, bytes, null, "first task complete screenshot", null);

        int num = (int) tasksList.stream().filter(tsk -> tsk.getSameTaskId() != null && tsk.getSameTaskId().equals(task.getId())).count();
        if (num > 0) {
            for (int i = 0; i < num; i++) {
                Task task2 = tasksList.stream().filter(tsk -> tsk.getSameTaskId() != null && tsk.getSameTaskId().equals(task.getId())).collect(Collectors.toList()).get(i);
                task2.setFinished(true);

                newAnswer = saveAnswer(newUser, task2, fileId, file, bytes, null, "first task complete + automatic solved this task", null);
                newUser.addTask(task2);
            }
        }

        String msg = getTextByCode("200_points"); //"Поздравляем! +200 баллов к твоему счету баллов. Для того чтобы продолжить игру, выбери компетенцию";
        String str = EmojiParser.parseToUnicode(":gift: :moneybag: :moneybag:" + msg);
        sendQuizChoice(chatId, newUser, str);

        return newUser;
    }

    private Answer saveAnswer(User newUser, Task task, String fileId, java.io.File file, byte[] bytes, String textAnswer, String comment, String fileName) {
        Answer answer = new Answer();
        answer.setUserId(newUser.getId());
        answer.setOwner(newUser.getUsername());
        answer.setEmail(newUser.getEmail());
        answer.setComment(comment);

        answer.setTaskId(task.getId());
        answer.setTaskCategory(task.getTitle());
        answer.setTaskLevel(task.getOrder());
        answer.setTaskName(task.getDescription());
        answer.setTextAnswer(textAnswer);
        answer.setPoint(task.getPoint());
        answer.setTlgFileId(fileId);
        answer.setFileOrigName(fileName);
        if (file != null) {
            answer.setAnswerFile(file);
            answer.setFileContent(bytes);
        }
        Answer newAnswer = answerService.save(answer);
        return newAnswer;
    }

    private User sendQuizChoice(long chatId, User newUser, String msg) {
        String[][] res = new String[][]{
                new String[]{QUIZ_BTN1, QUIZ_BTN2, QUIZ_BTN3},
                new String[]{QUIZ_BTN4, QUIZ_BTN5, QUIZ_BTN6},
                new String[]{QUIZ_BTN7, QUIZ_BTN8, QUIZ_BTN9},
                new String[]{EmojiParser.parseToUnicode(competBtn), EmojiParser.parseToUnicode(commentBtn), EmojiParser.parseToUnicode(myResultBtn), EmojiParser.parseToUnicode(helpBtn)}
        };

        //Finished tasks
        List<Task> tasks = getFinishedTasks(newUser.getId());
        newUser.setTasks(tasks);
        newUser = checkUserInListEx(newUser);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String name = getClearText(res[i][j], true);
                String taskRes = "";

                if (tasks != null && !tasks.isEmpty() && tasks.size() > 0) {
                    for (int k = 1; k < 4; k++) {
                        final int level = k;
                        int rs = (int) tasks.stream().filter(tsk -> tsk.getOrder() != null && tsk.getOrder().equals(level) && tsk.getTitle().equals(name)).count();
                        if (rs > 0) {
                            /////for cache
                            //Task task = tasks.stream().filter(tsk -> tsk.getOrder() != null && tsk.getOrder().equals(level) && tsk.getTitle().equals(name)).collect(Collectors.toList()).get(0);
                            //newUser.addTask(task);
                            ////
                            taskRes += ":green_heart:";
                        } else {
                            taskRes += ":yellow_heart:";
                        }
                    }
                } else {
                    taskRes += ":yellow_heart::yellow_heart::yellow_heart:";
                }

                res[i][j] = res[i][j] + EmojiParser.parseToUnicode(" [" + taskRes + "]");
            }
        }

        showButtons(chatId, (msg + " :point_down:"), res);
        return newUser;
    }

    private void showButtons(long chatId, String msg, String res[][]) {
        String msg1 = EmojiParser.parseToUnicode(msg);
        SendMessage send = new SendMessage(chatId, msg1);

        Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(res)
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional
        bot.execute(send.replyMarkup(replyKeyboardMarkup));
    }

    private String getClearText(String input, boolean rigthSide) {
        try {
            String res = getBackFromEmoji(input);
            int pos = rigthSide ? res.lastIndexOf(":") : res.indexOf(":");
            if (pos >= 0) {
                return (rigthSide ? res.substring(pos + 2) : res.substring(0, pos - 2));
            }
            return res;
        } catch (IndexOutOfBoundsException t) {
        }
        return "";
    }

    private String getTaskDescription(Task task) {
        String description = task.getDescription();
        String expFormat = getTextByCode("exp_format"); //Ожидаемый формат:
        String txtPoint = getTextByCode("task_points_given"); // Баллы за задание
        String format = (task.getResult() != null ? task.getResult().toLowerCase() : "");
        String point = (task.getPoint() != null ? ("\n\r[" + txtPoint + ": " + task.getPoint() + " :moneybag::moneybag: ]") : "");

        return (":green_book:" + description + "\n\r\n\r[:bellhop_bell: " + expFormat + ":  " + format + " :pushpin:]" + point);
    }

    private User handleButtonTaskNew(Long chatId, User user, String title, int lev) {
        String title1 = getClearText(title, true);

        //tasks from DB
        if (!(user.getTasks() != null && !user.getTasks().isEmpty())) {
            List<Task> tasksList = getFinishedTasks(user.getId());
            user.setTasks(tasksList);
        }
        Task task = getTaskByTitleLevel(title1, lev);
        String description = task.getDescription();

        int fin = (int) user.getTasks().stream().filter(tsk -> tsk.getTitle().equals(title1) && tsk.getOrder().intValue() == lev && tsk.isFinished()).count();
        if (fin > 0) {
            //Task description
            String txt = getTaskDescription(task);

            String desc = getTextByCode("task_finished"); //finished
            sendEmojiText(chatId, ":white_check_mark: " + desc + " :white_check_mark:" + "\n\r\n\r" + txt);
            return user;
        }

        if (task.getFileId() != null) {
            String files[] = task.getFileId().split(",");
            String type = task.getFileType();

            for (String fileId : files) {
                if (type.equals("photo")) {
                    SendPhoto photo = new SendPhoto(chatId, fileId.trim());
                    bot.execute(photo);
                } else {
                    SendDocument document = new SendDocument(chatId, fileId.trim());
                    bot.execute(document);
                }
            }
        }

        String txt = getTaskDescription(task);

        sendEmojiText(chatId, txt);

        String msg = getTextByCode("your_action");//Ваше действие
        String btns[][] = new String[][]{new String[]{EmojiParser.parseToUnicode(BTN_ANSWER), EmojiParser.parseToUnicode(BTN_BACK)}};
        showButtons(chatId, msg + " :point_down:", btns);

        user.setTempAttr(titleEx, title1);
        user.setTempAttr(CUR_LEVEL, lev);
        user.addTask(task);
        return user;
    }

    private User askInputAnswer(long chatId, User user) {
        if (user.getTempAttr(CUR_LEVEL) == null)
            return null;

        String msg = getTextByCode("input_your_answer");
        int messId = sendEmojiForceReply(chatId, ":writing_hand:" + msg);

        int curLevel = (int) user.getTempAttr(CUR_LEVEL);
        String level_message_id = level1_message_id;
        if (curLevel == 2) {
            level_message_id = level2_message_id;
        }
        if (curLevel == 3) {
            level_message_id = level3_message_id;
        }
        user.setTempAttr(level_message_id, messId);
        return user;
    }

    private int sendEmojiForceReply(long chatId, String text) {
        SendMessage send = new SendMessage(chatId, EmojiParser.parseToUnicode(text));
        send.replyMarkup(new ForceReply());
        SendResponse response = bot.execute(send);

        int messId = response.message().messageId();
        return messId;
    }

    private void showHelpMessage(long chatId) {
        String mess = getTextByCode("help_btn");
        sendEmojiText(chatId, mess + ":ambulance:");
    }

    private void showMyResult(long chatId, User newUser) {
        String mess = getTextByCode("result_points");
        int points = (newUser.getPoints() != null ? newUser.getPoints() : 0);
        sendEmojiText(chatId, ":moneybag: " + mess + ": " + points);
    }

    private User inputCommentRequest(long chatId, User user) {
        String msg = getTextByCode("comment_input");//"Введите комментарий";
        int messId = sendEmojiForceReply(chatId, ":point_up: :keyboard:" + msg);
        user.setTempAttr(input_comment_message_id, messId);
        return user;
    }

    private void botMissUndestand(long chatId) {
        String msg = getTextByCode("sorry_no_understand");
        sendEmojiText(chatId, msg + ":innocent:");
    }

    private Task getTaskByTitleLevel(String title, int level) {
        for (Task task : tasksList) {
            if (task.getTitle().equals(title) && task.getOrder().intValue() == level)
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

    private Category getCategoryById(long id) {
        for (Category category : categoriesList) {
            if (category.getId().equals(id))
                return category;
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

    private User checkMeEx(User user) {
        User user1 = userService.getUserByTlgId(user.getTlgId());
        if (user1 != null)
            return user1;

        //создадим в БД Новую
        user.setUsername(user.getTlgUsername() != null ? user.getTlgUsername() : user.getTlgFirstname());
        user.setEmail((user.getTlgUsername() != null ? user.getTlgUsername() : "xyz") + System.currentTimeMillis() + "@mail.xxx");
        user.setPassword("12345");

        return userService.save(user);
    }

    private User checkUserInListEx(User user) {
        if (user.getId() == null) {
            for (Map.Entry<Integer, User> entry : usersCache.entrySet()) {
                User curUsr = entry.getValue();
                if (curUsr.getId() != null && curUsr.getTlgId().equals(user.getTlgId())) {
                    return curUsr;
                }
            }
        } else {
            User curUsr = usersCache.get(user.getId());
            if (curUsr != null && curUsr.getTlgId().equals(user.getTlgId())) {
                if (curUsr.getId() == null) {
                    usersCache.put(user.getId(), user);
                    return user;
                }
            } else {
                usersCache.put(user.getId(), user);
                return user;
            }
            if(!user.getTasks().isEmpty() && user.getTasks().size() > 0){
                usersCache.put(user.getId(), user);
                return user;
            }
            return curUsr;
        }

        for (Map.Entry<Integer, User> entry : usersCache.entrySet()) {
            User curUsr = entry.getValue();
            if (user.getTlgId().equals(curUsr.getTlgId())) {
                return curUsr;
            }
        }

        if (user.getId() != null && usersCache.get(user.getId()) == null)
            usersCache.put(user.getId(), user);

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

            if (user.getTempAttr() != null && !user.getTempAttr().isEmpty()) {
                dbUser.setTempAttr(user.getTempAttr());
                dbUser.setTasks(user.getTasks());
            }

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
        usersCache.put(dbUser.getId(), dbUser);

        dbUser = userService.save(dbUser); //сохраним в БД
        ///
        dbUser.setTempAttr(user.getTempAttr());
        dbUser.setTasks(user.getTasks());
        return dbUser;
    }
}
