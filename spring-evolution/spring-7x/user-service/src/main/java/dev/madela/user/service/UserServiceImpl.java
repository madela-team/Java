package dev.madela.user.service;

import dev.madela.user.entity.UserEntity;
import dev.madela.user.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserEntity create(String name, String email) {
        if (repo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        return repo.save(new UserEntity(name, email, OffsetDateTime.now(ZoneOffset.UTC)));
    }

    @Override
    public UserEntity get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}
