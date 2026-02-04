package dev.madela.reminder.service;

import dev.madela.reminder.client.UserClient;
import dev.madela.reminder.dto.ReminderDto;
import dev.madela.reminder.dto.UserDto;
import dev.madela.reminder.entity.Reminder;
import dev.madela.reminder.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository repository;
    private final UserClient userClient;

    public ReminderServiceImpl(ReminderRepository repository,
                               UserClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }

    @Override
    public ReminderDto create(Long userId, String text, String remindAt) {

        // Получаем пользователя из user-service
        UserDto user = userClient.getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        LocalDateTime time = LocalDateTime.parse(remindAt.replace(" ", "T"));

        Reminder reminder = repository.save(new Reminder(userId, text, time));

        return toDto(reminder, user);
    }

    @Override
    public List<ReminderDto> findByUser(Long userId) {

        UserDto user = userClient.getUser(userId);

        return repository.findByUserId(userId)
                .stream()
                .map(r -> toDto(r, user))
                .collect(Collectors.toList());
    }

    private ReminderDto toDto(Reminder r, UserDto user) {
        return new ReminderDto(
                r.getId(),
                user,
                r.getText(),
                r.getRemindAt()
        );
    }
}
