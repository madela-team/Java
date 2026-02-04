package dev.madela.reminder.dto;

public class CreateReminderRequest {
    private Long userId;
    private String text;
    private String remindAt;

    public CreateReminderRequest() {}

    public Long getUserId() { return userId; }
    public String getText() { return text; }
    public String getRemindAt() { return remindAt; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setText(String text) { this.text = text; }
    public void setRemindAt(String remindAt) { this.remindAt = remindAt; }
}
