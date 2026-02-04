package dev.madela.service;

import dev.madela.dao.ReminderDao;
import dev.madela.model.Reminder;

import java.util.List;

public class ReminderServiceImpl implements ReminderService {

    private ReminderDao reminderDao;

    public void setReminderDao(ReminderDao reminderDao) {
        this.reminderDao = reminderDao;
    }

    public Reminder createReminder(Reminder reminder) {
        return reminderDao.create(reminder);
    }

    public Reminder getReminder(Long id) {
        return reminderDao.findById(id);
    }

    public List getUserReminders(Long userId) {
        return reminderDao.findAllByUserId(userId);
    }

}
