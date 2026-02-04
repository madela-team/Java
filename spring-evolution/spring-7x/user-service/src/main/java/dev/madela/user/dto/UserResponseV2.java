package dev.madela.user.dto;

import java.time.OffsetDateTime;

public record UserResponseV2(Long id, String name, String email, OffsetDateTime createdAt) {}
