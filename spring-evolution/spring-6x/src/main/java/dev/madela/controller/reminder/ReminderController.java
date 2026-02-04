package dev.madela.controller.reminder;

import dev.madela.controller.reminder.dto.CreateReminderRequest;
import dev.madela.controller.reminder.dto.ReminderResponse;
import dev.madela.reminder.entity.ReminderEntity;
import dev.madela.reminder.service.ReminderService;
import dev.madela.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminders;
    private final UserService users;

    public ReminderController(ReminderService reminders, UserService users) {
        this.reminders = reminders;
        this.users = users;
    }

    @PostMapping
    public ReminderResponse create(@Valid @RequestBody CreateReminderRequest req) {
        // проверка существования пользователя
        users.get(req.userId());

        LocalDateTime time = LocalDateTime.parse(req.remindAt());
        ReminderEntity r = reminders.create(req.userId(), req.text(), time);
        return new ReminderResponse(r.getId(), r.getUserId(), r.getText(), r.getRemindAt());
    }

    @GetMapping("/user/{userId}")
    public List<ReminderResponse> byUser(@PathVariable("userId") Long userId) {
        users.get(userId);

        return reminders.byUser(userId).stream()
                .map(r -> new ReminderResponse(r.getId(), r.getUserId(), r.getText(), r.getRemindAt()))
                .toList();
    }
}
