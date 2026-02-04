package dev.madela.reminder.service;

import dev.madela.reminder.entity.ReminderEntity;

import java.util.List;

public interface ReminderService {
    ReminderEntity create(Long userId, String text, String remindAt);
    List<ReminderEntity> byUser(Long userId);
}
