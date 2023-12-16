package com.example.tbot.service;

import com.example.tbot.config.BotConfig;
import com.example.tbot.model.Spring.*;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private String chosenLevel;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private RegisteredUsersRepository registeredUsersRepository;
    @Autowired
    private AdsRepository adsRepository;
    final BotConfig config;

    private static List<LocalTime> times = new ArrayList<>()
    {{
        add(LocalTime.of(13, 0));
        add(LocalTime.of(15, 0));
        add(LocalTime.of(17, 0));
    }
        @Override
        public String toString() {
            String res = "";
            for(int i = 0; i < times.size() - 1; i++)
                res += times.get(i) + ", ";
            return res+times.get(times.size()-1);
        }

    };
    static final String HELP_TEXT = "FreeEnglish - це розмовний клуб для тих, хто прагне втосконалювати English. " +
            "Або просто весело провести час з новими людьми:sparkles:\n\n" +
            "Щосуботи о " + times + " ти можеш до нас завітати - " +
            "приєднуйся до дружньої FreeEnglish family:blue_heart:\n";
    static final String ERROR_TEXT = "Error occurred: ";
    static final String CHOSEN_TIME = "Обери час івенту, який бажаєш відвідати:rocket:";
    static final String A = "A";
    static final String B = "B";
    static final String LINK_TO_TEST = "LINK_TO_TEST";
    static final String TIME_13 = "TIME_13";
    static final String TIME_15 = "TIME_15";
    static final String TIME_17 = "TIME_17";

    public TelegramBot(BotConfig config)
    {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Get a welcome message"));
        listOfCommands.add(new BotCommand("/help", "Info how to use bot"));
        listOfCommands.add(new BotCommand("/register", "Set your preferences"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }


    /**
     * Send response after user's command-message
     */
    void sendUpdatedMessage(String messageText, long chatID, Update update){
//        System.out.println("Entered to sendUpdateMessage: " + messageText);
        switch (messageText) {
            case "/start":
                registerUser(update.getMessage());
                startCommandReceived(chatID, update.getMessage().getChat().getFirstName());
                break;
            case "/help":
                sendMessage(chatID, EmojiParser.parseToUnicode(HELP_TEXT));
                //prepareAndSendMessage(chatID, HELP_TEXT);
                break;
//            case "/register":
//                register(chatID);
//                break;
            case "/send":
                break;
            default:
                sendMessage(chatID, "На жаль, не знаю відповіді");
                //prepareAndSendMessage(chatID, "Sorry, command was not recognized");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatID = update.getMessage().getChatId();

            if (messageText.contains("/send") && config.getOwnerId() == chatID) {
                var textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
                var users = userRepository.findAll();//hibernateUtils.getAllUsers();
                for (User user : users)//(UserEng user : users)
                    sendMessage(user.getId(), textToSend);//(user.getChat_id(), textToSend);
            } else {
                sendUpdatedMessage(messageText, chatID, update);
//                switch (messageText) {}
            }
        } else if (update.hasCallbackQuery()) {
            System.out.println(update);
            if(update.getCallbackQuery().getData().contains("TIME"))
                sendRegister(update);
            else
                sendUpdatedChoiceMessage(update);
//            String callbackData = update.getCallbackQuery().getData();
//            long messageId = update.getCallbackQuery().getMessage().getMessageId();
//            long chatId = update.getCallbackQuery().getMessage().getChatId();
//
//            switch (callbackData) {}
        }
    }

    private void registerUser(Message msg)
    {
//        if(hibernateUtils.getUserById(msg.getChatId())== null)
//        {
//            var chatID = msg.getChatId();
//            var chat = msg.getChat();
//            UserEng user = new UserEng();
//            user.setChat_id(chatID);
//            user.setFirstName(chat.getFirstName());
//            user.setLastName(chat.getLastName());
//            user.setUserName(chat.getUserName());
//            hibernateUtils.addUser(user);
//            System.out.println("User saved: " + user);
//        }
//        Using CrudRepository
        if (userRepository.findUserByUserName(msg.getChat().getUserName()) == null){//(userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatID = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();
            user.setId(chatID);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            userRepository.save(user);
            log.info("User saved: " + user);
        }
    }

    private void startCommandReceived(long chatID, String name) {
        String answer = EmojiParser.parseToUnicode("Привіт, " + name +
                ", ти завітав до FreeEnglish" + ":smiling_face_with_hearts:" +
                "\nТут ти знайдеш різні заходи для покращення твоїх розмовних навичків з англійської");
        log.info("Replied to user " + name);
        sendMessage(chatID, answer);
        System.out.println();
        String answer2 = "Обери свій рівень англійської)";

        String link = "https://test-english.com/level-test/";
        sendChoiceButtonsMessage(chatID, answer2, link);

//        executeMessage(message);
//        sendMessage(chatID, link);
    }
    private void sendChoiceButtonsMessage(long chatID, String text, String testLink){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatID));
        message.setText(text);
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> rowInLineLevels = new ArrayList<>();
        var buttonA = new InlineKeyboardButton();
        buttonA.setText("Elementary+");
        buttonA.setCallbackData(A);
        var buttonB = new InlineKeyboardButton();
        buttonB.setText("Intermediate+");
        buttonB.setCallbackData(B);
        List<InlineKeyboardButton> rowInLineTest = new ArrayList<>();
        var buttonTest = new InlineKeyboardButton();
        buttonTest.setText("Пройти тест");
        buttonTest.setUrl(testLink);
        buttonTest.setCallbackData(LINK_TO_TEST);

        rowInLineLevels.add(buttonA);
        rowInLineLevels.add(buttonB);
        rowInLineTest.add(buttonTest);

        rowsInLine.add(rowInLineLevels);
        rowsInLine.add(rowInLineTest);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }

    /**
     * Send response after user's choice (Knowledge level)
     * @param update
     */
    private void sendUpdatedChoiceMessage(Update update) {
        System.out.println("sendUpdatedChoiceMessage");
        String callbackData = update.getCallbackQuery().getData();// !!!
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        switch (callbackData) {
            case A -> {
                List<Event> events = (List<Event>) eventsRepository.findAll();
                List<String> eventsA = events.stream().filter(level -> level.getLevel().equals("A")).map(Event::getName).toList();
                sendMessage(chatId, eventsMessage(eventsA, "For levels A1+"));
                registerOnEvent(EmojiParser.parseToUnicode(CHOSEN_TIME), chatId);
            }
            case B -> {
                List<Event> events = (List<Event>) eventsRepository.findAll();
                List<String> eventsB = events.stream().filter(level -> level.getLevel().equals("B")).map(Event::getName).toList();
                sendMessage(chatId, eventsMessage(eventsB, "For levels B1+"));
                registerOnEvent(EmojiParser.parseToUnicode(CHOSEN_TIME), chatId);
            }
            // URL was set when LINK_TO_TEST button was created in sendChoiceButtonsMessage()
            case LINK_TO_TEST -> {}
//            {
//                String link = "https://test-english.com/level-test/";
//                String a = "[inline URL](" + link + ")";
//                String s = "<a href=\"" + link + "\">Пройти тест</a>";
//                sendMessage(chatId, s);
//            }
        }

        String level = update.getCallbackQuery().getData();
        chosenLevel = level;
        List<Event> events = ((List<Event>) eventsRepository.findAll()).stream().filter(event -> event.getLevel().equals(level)).toList();//.map(Event::getTime);
        System.out.println(events);
    }
    private String eventsMessage(List<String> events, String textLevel){
        String res = "<strong>" + textLevel.toUpperCase() + "</strong>\n\n";
        res += "<strong>Час початку: </strong>" + times + "\n" +
                ":computer:Перед початком зустрічі надішлю посилання на Zoom-зустріч\n\n" +
                "<strong>Топікс найближчих зустрічей:</strong>\n";
        for(int i = 0; i < events.size(); i++){
            res += ":white_medium_square:\"" + events.get(i) + "\"\n";
        }
        return EmojiParser.parseToUnicode(res);
    }

    private void registerOnEvent(String text, long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        List<InlineKeyboardButton> rowInLineTimes = new ArrayList<>();
        var buttonFirst = new InlineKeyboardButton();
        buttonFirst.setText(times.get(0).toString());
        buttonFirst.setCallbackData(TIME_13);
        var buttonSecond = new InlineKeyboardButton();
        buttonSecond.setText(times.get(1).toString());
        buttonSecond.setCallbackData(TIME_15);
        var buttonThird = new InlineKeyboardButton();
        buttonThird.setText(times.get(2).toString());
        buttonThird.setCallbackData(TIME_17);

        rowInLineTimes.add(buttonFirst);
        rowInLineTimes.add(buttonSecond);
        rowInLineTimes.add(buttonThird);

        rowsInLine.add(rowInLineTimes);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }

    /**
     * Send response after user's choice (Knowledge level)
     * @param update
     */
    private void sendRegister(Update update) {
        System.out.println("sendRegister");
        String callbackData = update.getCallbackQuery().getData();// !!!
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String username = update.getCallbackQuery().getMessage().getChat().getUserName();
        switch (callbackData) {
            case TIME_13 -> {
                sendSuccessfulRegistration(String.valueOf(times.get(0)), chatId, messageId, username);
            }
            case TIME_15 -> {
                sendSuccessfulRegistration(String.valueOf(times.get(1)), chatId, messageId, username);
            }
            case TIME_17 -> {
                sendSuccessfulRegistration(String.valueOf(times.get(2)), chatId, messageId, username);
            }
        }
    }

    private void sendSuccessfulRegistration(String time, long chatId, long messageId, String username){
        System.out.println("sendSuccessfulRegistration");

        System.out.println(chosenLevel + " - " + time);
        Event event = eventsRepository.findEventByLevelAndTime(chosenLevel, LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")));
        System.out.println(event);
        User user = userRepository.findUserByUserName(username);
        System.out.println(user);

        RegisteredUsers registeredUsers = new RegisteredUsers(user.getId(), user.getUserName(), event.getId(), event.getTime());

        String text = "Вітаю, ти зареєструвався на івент о " + time + "\n:link:Посилання надішлю перед зустріччю";
        if(registeredUsersRepository.existsByUserAndEvent(user.getId(), event.getId()))
            text = "Нагадування, ти вже зареєстрований на івент о " + time + "\n:link:Посилання надішлю перед зустріччю";
        else
            registeredUsersRepository.save(registeredUsers);

        executeEditMessageText(EmojiParser.parseToUnicode(text), chatId, messageId);
    }
    private void executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setParseMode(ParseMode.HTML);
        messageText.setChatId(String.valueOf(chatId));
        messageText.setText(text);
        messageText.setMessageId((int) messageId);
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void sendMessage(long chatID, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatID));
        message.setText(textToSend);
        executeMessage(message);
    }

    /**
     * Почему не сделать это в sendMessage???
     * наверное потому что она used also in executeEditMessageText
     * @param message
     */
    private void executeMessage(SendMessage message) {
        message.setParseMode(ParseMode.HTML);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void register(Long chatID) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatID));
        message.setText("Do you really want to register?");

    }

    @Scheduled(cron = "${cron.scheduler}")//(fixedRate = 600000)
    private void sendAds() {
//        var ads = adsRepository.findAll();
        var users = (List<RegisteredUsers>)registeredUsersRepository.findAll();
        var events = (List<Event>)eventsRepository.findAll();

        LocalTime now = LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), 0);

        for (RegisteredUsers user : users)
        {
            LocalTime reminderTime = user.getTime().minusMinutes(60);
            System.out.println(now);
            if (now.equals(reminderTime))// || now.isBefore(reminderTime))
            {
                System.err.println("reminder");
//                for (RegisteredUsers user : users) {
                    sendMessage(user.getUser(), EmojiParser.parseToUnicode(":alarm_clock:Івент почнеться через годину\n" +
                            "Залишаю посилання на Zoom-конференцію\n" + "//link//"));
//                }
            }
        }
    }
}
