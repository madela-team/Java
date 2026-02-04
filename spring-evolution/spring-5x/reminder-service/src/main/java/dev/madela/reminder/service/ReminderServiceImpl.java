package dev.madela.reminder.service;

import dev.madela.reminder.client.UserClient;
import dev.madela.reminder.dto.CreateReminderRequest;
import dev.madela.reminder.dto.ReminderDto;
import dev.madela.reminder.dto.UserDto;
import dev.madela.reminder.entity.Reminder;
import dev.madela.reminder.repository.ReminderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository repo;
    private final UserClient userClient;

    public ReminderServiceImpl(ReminderRepository repo, UserClient userClient) {
        this.repo = repo;
        this.userClient = userClient;
    }

    @Override
    public Mono<ReminderDto> create(CreateReminderRequest req) {
        if (req.getUserId() == null || req.getText() == null || req.getRemindAt() == null) {
            return Mono.error(new IllegalArgumentException("userId/text/remindAt required"));
        }

        LocalDateTime time = parseTime(req.getRemindAt());

        Mono<UserDto> userMono = userClient.getUser(req.getUserId());

        return userMono.flatMap(user ->
                repo.save(new Reminder(null, req.getUserId(), req.getText(), time))
                   .map(saved -> new ReminderDto(saved.getId(), user, saved.getText(), saved.getRemindAt()))
        );
    }

    @Override
    public Flux<ReminderDto> findByUser(Long userId) {
        Mono<UserDto> userMono = userClient.getUser(userId).cache();

        return userMono.flatMapMany(user ->
                repo.findByUserId(userId)
                   .map(r -> new ReminderDto(r.getId(), user, r.getText(), r.getRemindAt()))
        );
    }

    private LocalDateTime parseTime(String remindAt) {
        String normalized = remindAt.contains("T") ? remindAt : remindAt.replace(" ", "T");
        return LocalDateTime.parse(normalized);
    }
}
