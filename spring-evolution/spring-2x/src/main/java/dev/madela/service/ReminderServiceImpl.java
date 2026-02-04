package dev.madela.service;

import dev.madela.dao.ReminderDao;
import dev.madela.model.Reminder;

import java.util.Date;
import java.util.List;

public class ReminderServiceImpl implements ReminderService {

    private ReminderDao reminderDao;

    public void setReminderDao(ReminderDao reminderDao) {
        this.reminderDao = reminderDao;
    }

    public Reminder createReminder(Long userId, String text, Date remindAt) {

        Reminder r = reminderDao.create(userId, text, remindAt);

        return r;
    }

    public List<Reminder> getByUserId(Long userId) {
        return reminderDao.findByUserId(userId);
    }
}
