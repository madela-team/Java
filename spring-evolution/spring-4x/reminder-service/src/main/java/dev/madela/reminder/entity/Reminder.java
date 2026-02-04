package dev.madela.reminder.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(name = "remind_at", nullable = false)
    private LocalDateTime remindAt;

    protected Reminder() {
    }

    public Reminder(Long userId, String text, LocalDateTime remindAt) {
        this.userId = userId;
        this.text = text;
        this.remindAt = remindAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getRemindAt() {
        return remindAt;
    }
}
