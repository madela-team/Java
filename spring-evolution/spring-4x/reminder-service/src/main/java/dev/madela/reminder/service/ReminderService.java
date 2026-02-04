package dev.madela.reminder.service;

import dev.madela.reminder.dto.ReminderDto;

import java.util.List;

public interface ReminderService {

    ReminderDto create(Long userId, String text, String remindAt);

    List<ReminderDto> findByUser(Long userId);
}
