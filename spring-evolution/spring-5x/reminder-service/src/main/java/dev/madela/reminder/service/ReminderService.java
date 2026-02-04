package dev.madela.reminder.service;

import dev.madela.reminder.dto.CreateReminderRequest;
import dev.madela.reminder.dto.ReminderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReminderService {
    Mono<ReminderDto> create(CreateReminderRequest req);
    Flux<ReminderDto> findByUser(Long userId);
}
