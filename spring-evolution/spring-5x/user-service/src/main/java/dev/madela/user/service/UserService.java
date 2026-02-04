package dev.madela.user.service;

import dev.madela.user.dto.CreateUserRequest;
import dev.madela.user.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDto> create(CreateUserRequest req);
    Mono<UserDto> findById(Long id);
}
