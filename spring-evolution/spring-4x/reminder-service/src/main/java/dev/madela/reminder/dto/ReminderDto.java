package dev.madela.reminder.dto;

import java.time.LocalDateTime;

public class ReminderDto {

    private Long id;
    private UserDto user;
    private String text;
    private LocalDateTime remindAt;

    public ReminderDto() {
    }

    public ReminderDto(Long id, UserDto user, String text, LocalDateTime remindAt) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.remindAt = remindAt;
    }

    public Long getId() {
        return id;
    }

    public UserDto getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getRemindAt() {
        return remindAt;
    }
}
