package dev.madela.user.controller;

import dev.madela.user.entity.UserEntity;
import dev.madela.user.service.UserService;
import dev.madela.user.dto.CreateUserRequest;
import dev.madela.user.dto.UserResponseV2;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserControllerV2 {

    private final UserService service;

    public UserControllerV2(UserService service) {
        this.service = service;
    }

    @PostMapping(version = "2")
    public UserResponseV2 create(@Valid @RequestBody CreateUserRequest req) {
        UserEntity u = service.create(req.name(), req.email());
        return new UserResponseV2(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt());
    }

    @GetMapping(value = "/{id}", version = "2")
    public UserResponseV2 get(@PathVariable("id") Long id) {
        UserEntity u = service.get(id);
        return new UserResponseV2(u.getId(), u.getName(), u.getEmail(), u.getCreatedAt());
    }
}
