package dev.madela.service;

import dev.madela.model.Reminder;

import java.util.Date;
import java.util.List;

public interface ReminderService {
    Reminder createReminder(Long userId, String text, Date remindAt);
    List<Reminder> getByUserId(Long userId);
}
