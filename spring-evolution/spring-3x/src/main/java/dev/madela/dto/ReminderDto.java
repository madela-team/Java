package dev.madela.dto;

public class ReminderDto {

    private Long id;
    private UserDto user;
    private String text;
    private String remindAt;

    public ReminderDto() {
    }

    public ReminderDto(Long id, UserDto user, String text, String remindAt) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.remindAt = remindAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(String remindAt) {
        this.remindAt = remindAt;
    }
}
