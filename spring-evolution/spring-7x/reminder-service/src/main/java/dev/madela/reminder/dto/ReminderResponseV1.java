package dev.madela.reminder.dto;

public record ReminderResponseV1(Long id, Long userId, String text, String remindAt) {}
