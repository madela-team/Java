package dev.madela.dao;

import dev.madela.model.User;

public interface UserDao {

    User findById(Long id);

    void create(User user);
}
