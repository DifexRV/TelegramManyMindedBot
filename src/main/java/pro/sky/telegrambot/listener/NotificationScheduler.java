package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repositories.NotificationTaskRepositories;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationScheduler {

    private Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);

    @Autowired
    private NotificationTaskRepositories notificationTaskRepositories;

    @Autowired
    private TelegramBot telegramBot;

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotification() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> Tasks = notificationTaskRepositories.findByNotificationDate(now);
        for (NotificationTask task : Tasks) {
            try {
                logger.info("Отправка уведомления: {}", task.getMessageText());
                telegramBot.execute(new SendMessage(task.getChatId(), task.getMessageText()));
                logger.info("Уведомление отправлено");
                notificationTaskRepositories.delete(task);
            } catch (Exception e) {
                logger.error("Error processing task: {}", e);
            }
        }

    }
}
