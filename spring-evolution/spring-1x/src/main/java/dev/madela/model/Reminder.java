package dev.madela.model;

import java.sql.Timestamp;

public class Reminder {

    private Long id;
    private Long userId;     // связь с User
    private String text;
    private Timestamp remindAt;

    public Reminder() {}

    public Reminder(Long id, Long userId, String text, Timestamp remindAt) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.remindAt = remindAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(Timestamp remindAt) {
        this.remindAt = remindAt;
    }
}
