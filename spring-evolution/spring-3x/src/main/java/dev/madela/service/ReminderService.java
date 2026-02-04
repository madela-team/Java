package dev.madela.service;

import java.util.Date;
import java.util.List;

import dev.madela.model.Reminder;

public interface ReminderService {

    Reminder createReminder(Long userId, String text, Date remindAt);

    List<Reminder> getByUserId(Long userId);
}
