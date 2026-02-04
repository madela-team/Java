package dev.madela.reminder.service;

import dev.madela.reminder.entity.ReminderEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderService {
    ReminderEntity create(Long userId, String text, LocalDateTime remindAt);
    List<ReminderEntity> byUser(Long userId);
}
