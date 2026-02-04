package dev.madela.model;

import java.util.Date;

public class Reminder {

    private Long id;
    private Long userId;
    private String text;
    private Date remindAt;

    public Reminder() {}

    public Reminder(Long id, Long userId, String text, Date remindAt) {
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

    public Date getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(Date remindAt) {
        this.remindAt = remindAt;
    }
}
