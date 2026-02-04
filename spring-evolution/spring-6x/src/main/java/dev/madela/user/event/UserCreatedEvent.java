package dev.madela.user.event;

public record UserCreatedEvent(Long userId, String email) {}
