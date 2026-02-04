package dev.madela.service;

import dev.madela.model.User;

public interface UserService {
    User createUser(String name, String email);
    User getUser(Long id);
}
