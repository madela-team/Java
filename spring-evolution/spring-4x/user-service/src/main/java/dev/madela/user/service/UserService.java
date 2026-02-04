package dev.madela.user.service;

import dev.madela.user.model.User;

import java.util.concurrent.CompletableFuture;

public interface UserService {

    User create(String name, String email);

    CompletableFuture<User> getUserAsync(Long id);
}
