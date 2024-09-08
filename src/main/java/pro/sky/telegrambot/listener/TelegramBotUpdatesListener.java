package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import liquibase.pro.packaged.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repositories.NotificationTaskRepositories;


import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private final NotificationTaskRepositories notificationTaskRepositories;

    public TelegramBotUpdatesListener(NotificationTaskRepositories notificationTaskRepositories) {
        this.notificationTaskRepositories = notificationTaskRepositories;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            try {
                logger.info("Processing update: {}", update);

                if (update.message() != null) {
                    Message message = update.message();
                    if (message.text() != null && message.text().toLowerCase().equals("/start")) {
                        telegramBot.execute(new SendMessage(message.chat().id(), "Привет! Я твой бот. Что ты хочешь сделать?"));
                    } else {
                        Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
                        Matcher matcher = pattern.matcher(update.message().text());

                        if (matcher.find()) {
                            String dateTimeString = matcher.group(1);
                            String notificationText = matcher.group(3);
                            logger.info("dateTimeString: {}", dateTimeString);
                            logger.info("Notification text: {}", notificationText);

                            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

                            NotificationTask notificationTask = new NotificationTask(dateTime, notificationText, message.chat().id());
                            logger.info("NotificationTask: {}", notificationTask);

                            notificationTaskRepositories.save(notificationTask);

                            telegramBot.execute(new SendMessage(message.chat().id(), "Напоминание создано!"));
                        } else {
                            telegramBot.execute(new SendMessage(message.chat().id(), "Неверный формат сообщения. Используйте формат: 01.01.2022 20:00 Сделать домашнюю работу"));
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error processing update: {}", e);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}