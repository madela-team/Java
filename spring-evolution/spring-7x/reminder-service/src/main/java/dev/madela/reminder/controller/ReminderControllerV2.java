package dev.madela.reminder.controller;

import dev.madela.reminder.client.UserHttpClient;
import dev.madela.reminder.dto.UserDto;
import dev.madela.reminder.entity.ReminderEntity;
import dev.madela.reminder.service.ReminderService;
import dev.madela.reminder.dto.CreateReminderRequest;
import dev.madela.reminder.dto.ReminderResponseV2;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderControllerV2 {

    private final ReminderService service;
    private final UserHttpClient users;

    public ReminderControllerV2(ReminderService service, UserHttpClient users) {
        this.service = service;
        this.users = users;
    }

    @PostMapping(version = "2")
    public ReminderResponseV2 create(@Valid @RequestBody CreateReminderRequest req) {
        ReminderEntity r = service.create(req.userId(), req.text(), req.remindAt());
        UserDto u = users.getUser(req.userId());
        return new ReminderResponseV2(r.getId(), u, r.getText(), r.getRemindAt().toString());
    }

    @GetMapping(value = "/{userId}", version = "2")
    public List<ReminderResponseV2> byUser(@PathVariable("userId") Long userId) {
        UserDto u = users.getUser(userId);
        return service.byUser(userId).stream()
                .map(r -> new ReminderResponseV2(r.getId(), u, r.getText(), r.getRemindAt().toString()))
                .toList();
    }
}
