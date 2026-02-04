package dev.madela.reminder.service;

import dev.madela.reminder.entity.ReminderEntity;
import dev.madela.reminder.repo.ReminderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository repo;

    public ReminderServiceImpl(ReminderRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public ReminderEntity create(Long userId, String text, LocalDateTime remindAt) {
        return repo.save(new ReminderEntity(userId, text, remindAt));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReminderEntity> byUser(Long userId) {
        return repo.findByUserId(userId);
    }
}
