package dev.madela.user.service;

import dev.madela.user.dao.UserDao;
import dev.madela.user.model.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User create(String name, String email) {
        Long id = userDao.create(name, email);
        return new User(id, name, email);
    }

    /**
     * Асинхронность Spring 4.x:
     * - @Async
     * - отдельный thread pool
     */
    @Override
    @Async
    public CompletableFuture<User> getUserAsync(Long id) {
        return CompletableFuture.completedFuture(
                userDao.findById(id)
        );
    }
}
