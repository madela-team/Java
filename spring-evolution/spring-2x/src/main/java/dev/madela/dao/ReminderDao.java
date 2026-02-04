package dev.madela.dao;

import dev.madela.model.Reminder;

import java.util.List;

public interface ReminderDao {

    Reminder create(Long userId, String text, java.util.Date remindAt);

    List<Reminder> findByUserId(Long userId);
}
