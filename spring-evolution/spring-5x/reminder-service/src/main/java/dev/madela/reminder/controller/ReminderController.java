package dev.madela.reminder.controller;

import dev.madela.reminder.dto.CreateReminderRequest;
import dev.madela.reminder.dto.ReminderDto;
import dev.madela.reminder.service.ReminderService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    private final ReminderService service;

    public ReminderController(ReminderService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<ReminderDto> create(@RequestBody CreateReminderRequest req) {
        return service.create(req);
    }

    @GetMapping
    public Flux<ReminderDto> byUser(@RequestParam("userId") Long userId) {
        return service.findByUser(userId);
    }
}
