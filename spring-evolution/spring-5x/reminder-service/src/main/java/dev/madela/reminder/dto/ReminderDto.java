package dev.madela.reminder.dto;

import java.time.LocalDateTime;

public class ReminderDto {
    private Long id;
    private UserDto user;
    private String text;
    private LocalDateTime remindAt;

    public ReminderDto() {}

    public ReminderDto(Long id, UserDto user, String text, LocalDateTime remindAt) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.remindAt = remindAt;
    }

    public Long getId() { return id; }
    public UserDto getUser() { return user; }
    public String getText() { return text; }
    public LocalDateTime getRemindAt() { return remindAt; }

    public void setId(Long id) { this.id = id; }
    public void setUser(UserDto user) { this.user = user; }
    public void setText(String text) { this.text = text; }
    public void setRemindAt(LocalDateTime remindAt) { this.remindAt = remindAt; }
}
