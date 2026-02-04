package dev.madela.reminder.listener;

import dev.madela.reminder.service.ReminderService;
import dev.madela.user.event.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserEventsListener {

    private static final Logger log = LoggerFactory.getLogger(UserEventsListener.class);

    private final ReminderService reminders;

    public UserEventsListener(ReminderService reminders) {
        this.reminders = reminders;
    }

    @ApplicationModuleListener
    void on(UserCreatedEvent event) {

        // доменное действие в другом модуле: после регистрации - приветственное напоминание
        reminders.create(
                event.userId(),
                "Это твоё первое напоминание",
                LocalDateTime.now().plusMinutes(5)
        );

        log.info("[modulith] welcome reminder created for userId={}", event.userId());
    }
}
