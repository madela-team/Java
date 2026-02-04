package dev.madela.controller.reminder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReminderRequest(
        @NotNull Long userId,
        @NotBlank @Size(max = 500) String text,
        @NotBlank String remindAt // ISO строка, напр: 2026-01-19T15:30:00
) {}
