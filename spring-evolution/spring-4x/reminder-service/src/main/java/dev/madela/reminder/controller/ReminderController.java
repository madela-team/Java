package dev.madela.reminder.controller;

import dev.madela.reminder.dto.CreateReminderRequest;
import dev.madela.reminder.dto.ReminderDto;
import dev.madela.reminder.service.ReminderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService service;

    public ReminderController(ReminderService service) {
        this.service = service;
    }

    @PostMapping
    public Callable<ReminderDto> create(@RequestBody CreateReminderRequest req) {
        return () -> service.create(req.getUserId(), req.getText(), req.getRemindAt());
    }

    @GetMapping("/user/{userId}")
    public Callable<List<ReminderDto>> byUser(@PathVariable Long userId) {
        return () -> service.findByUser(userId);
    }
}
