package dev.madela.reminder.service;

import dev.madela.reminder.client.UserHttpClient;
import dev.madela.reminder.entity.ReminderEntity;
import dev.madela.reminder.repo.ReminderRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository repo;
    private final UserHttpClient users;

    public ReminderServiceImpl(ReminderRepository repo, UserHttpClient users) {
        this.repo = repo;
        this.users = users;
    }

    @Override
    public ReminderEntity create(Long userId, String text, String remindAt) {
        // проверяем, что юзер существует (через HTTP interface client)
        users.getUser(userId);

        OffsetDateTime when = OffsetDateTime.parse(remindAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return repo.save(new ReminderEntity(userId, text, when, OffsetDateTime.now(ZoneOffset.UTC)));
    }

    @Override
    public List<ReminderEntity> byUser(Long userId) {
        return repo.findByUserId(userId);
    }
}
