package dev.madela.user.service;

import dev.madela.user.entity.UserEntity;
import dev.madela.user.event.UserCreatedEvent;
import dev.madela.user.repo.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final ApplicationEventPublisher events;

    public UserServiceImpl(UserRepository repo, ApplicationEventPublisher events) {
        this.repo = repo;
        this.events = events;
    }

    @Override
    @Transactional
    public UserEntity create(String name, String email) {
        UserEntity saved = repo.save(new UserEntity(name, email));
        // Демонстрация modulith: доменное событие между модулями
        events.publishEvent(new UserCreatedEvent(saved.getId(), saved.getEmail()));
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity get(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}
