package dev.madela.dao;

import dev.madela.model.Reminder;

import java.util.List;

public interface ReminderDao {
    Reminder findById(Long id);
    Reminder create(Reminder r);
    List findAllByUserId(Long userId);
}
