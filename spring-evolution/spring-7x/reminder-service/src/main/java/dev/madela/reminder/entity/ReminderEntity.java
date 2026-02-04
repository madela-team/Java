package dev.madela.reminder.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "reminders")
public class ReminderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String text;

    @Column(name = "remind_at", nullable = false)
    private OffsetDateTime remindAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected ReminderEntity() {}

    public ReminderEntity(Long userId, String text, OffsetDateTime remindAt, OffsetDateTime createdAt) {
        this.userId = userId;
        this.text = text;
        this.remindAt = remindAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getText() { return text; }
    public OffsetDateTime getRemindAt() { return remindAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
