package dev.madela.reminder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReminderRequest(
        @NotNull Long userId,
        @NotBlank String text,
        @NotBlank String remindAt
) {}
