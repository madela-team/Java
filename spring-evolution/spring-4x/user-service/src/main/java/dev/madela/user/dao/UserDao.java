package dev.madela.user.dao;

import dev.madela.user.model.User;

public interface UserDao {

    User findById(Long id);

    Long create(String name, String email);
}
