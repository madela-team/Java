package dev.madela.reminder.repository;

import dev.madela.reminder.entity.Reminder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ReminderRepository extends ReactiveCrudRepository<Reminder, Long> {
    Flux<Reminder> findByUserId(Long userId);
}
