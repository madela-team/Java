package dev.madela.reminder.dto;

import java.time.OffsetDateTime;

public record UserDto(Long id, String name, String email, OffsetDateTime createdAt) {}
