package dev.madela.reminder.dto;

public class CreateReminderRequest {
        private Long userId;
        private String text;
        private String remindAt; // "2025-12-31T15:30:00"

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getRemindAt() { return remindAt; }
        public void setRemindAt(String remindAt) { this.remindAt = remindAt; }
    }