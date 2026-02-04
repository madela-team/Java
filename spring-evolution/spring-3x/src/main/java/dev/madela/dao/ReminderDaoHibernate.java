package dev.madela.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.madela.model.Reminder;

@Repository
public class ReminderDaoHibernate implements ReminderDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Reminder findById(Long id) {
        return (Reminder) currentSession().get(Reminder.class, id);
    }

    public Long save(Reminder reminder) {
        return (Long) currentSession().save(reminder);
    }

    @SuppressWarnings("unchecked")
    public List<Reminder> findByUserId(Long userId) {
        // Используем JOIN FETCH, чтобы загрузить User вместе с Reminder
        return currentSession()
                .createQuery("select r from Reminder r JOIN FETCH r.user where r.user.id = :userId order by r.remindAt asc")
                .setLong("userId", userId.longValue())
                .list();
    }
}