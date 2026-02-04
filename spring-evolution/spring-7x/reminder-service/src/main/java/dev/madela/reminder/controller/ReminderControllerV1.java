package dev.madela.reminder.controller;

import dev.madela.reminder.entity.ReminderEntity;
import dev.madela.reminder.service.ReminderService;
import dev.madela.reminder.dto.CreateReminderRequest;
import dev.madela.reminder.dto.ReminderResponseV1;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderControllerV1 {

    private final ReminderService service;

    public ReminderControllerV1(ReminderService service) {
        this.service = service;
    }

    @PostMapping(version = "1")
    public ReminderResponseV1 create(@Valid @RequestBody CreateReminderRequest req) {
        ReminderEntity r = service.create(req.userId(), req.text(), req.remindAt());
        return new ReminderResponseV1(r.getId(), r.getUserId(), r.getText(), r.getRemindAt().toString());
    }

    @GetMapping(value = "/{userId}", version = "1")
    public List<ReminderResponseV1> byUser(@PathVariable("userId") Long userId) {
        return service.byUser(userId).stream()
                .map(r -> new ReminderResponseV1(r.getId(), r.getUserId(), r.getText(), r.getRemindAt().toString()))
                .toList();
    }
}
