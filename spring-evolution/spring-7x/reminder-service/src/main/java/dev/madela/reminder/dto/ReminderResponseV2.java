package dev.madela.reminder.dto;

public record ReminderResponseV2(Long id, UserDto user, String text, String remindAt) {}
