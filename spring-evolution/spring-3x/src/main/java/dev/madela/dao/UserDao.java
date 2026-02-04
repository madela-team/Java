package dev.madela.dao;

import dev.madela.model.User;

public interface UserDao {

    User findById(Long id);

    User findByEmail(String email);

    Long save(User user);
}
