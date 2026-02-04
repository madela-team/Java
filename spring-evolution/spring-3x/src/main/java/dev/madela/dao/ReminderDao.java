package dev.madela.dao;

import java.util.List;

import dev.madela.model.Reminder;

public interface ReminderDao {

    Reminder findById(Long id);

    Long save(Reminder reminder);

    List<Reminder> findByUserId(Long userId);
}
