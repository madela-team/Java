package dev.madela.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.madela.dao.ReminderDao;
import dev.madela.dao.UserDao;
import dev.madela.model.Reminder;
import dev.madela.model.User;

@Service
public class ReminderServiceImpl implements ReminderService {

    private ReminderDao reminderDao;
    private UserDao userDao;

    @Autowired
    public void setReminderDao(ReminderDao reminderDao) {
        this.reminderDao = reminderDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public Reminder createReminder(Long userId, String text, Date remindAt) {
        User user = userDao.findById(userId);
        if (user == null) {
            return null;
        }

        Reminder r = new Reminder();
        r.setUser(user);
        r.setText(text);
        r.setRemindAt(remindAt);

        Long id = reminderDao.save(r);
        r.setId(id);

        return r;
    }

    @Transactional(readOnly = true)
    public List<Reminder> getByUserId(Long userId) {
        return reminderDao.findByUserId(userId);
    }
}