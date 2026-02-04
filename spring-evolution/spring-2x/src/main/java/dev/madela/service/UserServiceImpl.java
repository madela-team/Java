package dev.madela.service;

import dev.madela.dao.UserDao;
import dev.madela.model.User;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(String name, String email) {
        return userDao.create(name, email);
    }

    public User getUser(Long id) {
        return userDao.findById(id);
    }
}
