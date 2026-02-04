package dev.madela.service;

import dev.madela.model.Reminder;

import java.util.List;

public interface ReminderService {
    Reminder createReminder(Reminder reminder);
    Reminder getReminder(Long id);
    List getUserReminders(Long userId);
}
