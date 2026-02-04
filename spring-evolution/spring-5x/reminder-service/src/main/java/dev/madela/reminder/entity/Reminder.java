package dev.madela.reminder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("reminders")
public class Reminder {

    @Id
    private Long id;

    private Long userId;
    private String text;
    private LocalDateTime remindAt;

    public Reminder() {}

    public Reminder(Long id, Long userId, String text, LocalDateTime remindAt) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.remindAt = remindAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getText() { return text; }
    public LocalDateTime getRemindAt() { return remindAt; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setText(String text) { this.text = text; }
    public void setRemindAt(LocalDateTime remindAt) { this.remindAt = remindAt; }
}
