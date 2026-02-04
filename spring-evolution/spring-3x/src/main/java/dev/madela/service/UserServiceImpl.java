package dev.madela.service;

import org.springframework.beans.factory.annotation.Autowired; // <-- Добавить импорт
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.madela.dao.UserDao;
import dev.madela.model.User;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    // Setter injection
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public User createUser(String name, String email) {
        User existing = userDao.findByEmail(email);
        if (existing != null) {
            return null;
        }

        User u = new User();
        u.setName(name);
        u.setEmail(email);

        Long id = userDao.save(u);
        u.setId(id);

        return u;
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userDao.findById(id);
    }
}