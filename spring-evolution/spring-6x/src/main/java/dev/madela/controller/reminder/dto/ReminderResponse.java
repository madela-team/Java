package dev.madela.controller.reminder.dto;

import java.time.LocalDateTime;

public record ReminderResponse(Long id, Long userId, String text, LocalDateTime remindAt) {}
