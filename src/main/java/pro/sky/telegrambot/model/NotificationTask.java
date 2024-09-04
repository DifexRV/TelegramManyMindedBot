package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "notification_date")
    private LocalDateTime notificationDate;

    public NotificationTask() {

    }

    public NotificationTask(LocalDateTime notificationDate, String messageText, long chatId) {
        this.notificationDate = notificationDate;
        this.messageText = messageText;
        this.chatId = chatId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", messageText='" + messageText + '\'' +
                ", notificationDate=" + notificationDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return id == that.id && chatId == that.chatId && Objects.equals(messageText, that.messageText) && Objects.equals(notificationDate, that.notificationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, messageText, notificationDate);
    }

}
