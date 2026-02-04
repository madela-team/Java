package dev.madela.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.madela.model.User;

@Repository
public class UserDaoHibernate implements UserDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public User findById(Long id) {
        return (User) currentSession().get(User.class, id);
    }

    @SuppressWarnings("unchecked")
    public User findByEmail(String email) {
        List<User> list = currentSession()
                .createQuery("from User u where u.email = :email")
                .setString("email", email)
                .setMaxResults(1)
                .list();

        return list.isEmpty() ? null : list.get(0);
    }

    public Long save(User user) {
        return (Long) currentSession().save(user);
    }
}