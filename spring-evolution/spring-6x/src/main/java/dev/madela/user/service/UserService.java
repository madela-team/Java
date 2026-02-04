package dev.madela.user.service;

import dev.madela.user.entity.UserEntity;

public interface UserService {
    UserEntity create(String name, String email);
    UserEntity get(Long id);
}
