package dev.madela.user.controller;

import dev.madela.user.entity.UserEntity;
import dev.madela.user.service.UserService;
import dev.madela.user.dto.CreateUserRequest;
import dev.madela.user.dto.UserResponseV1;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserControllerV1 {

    private final UserService service;

    public UserControllerV1(UserService service) {
        this.service = service;
    }

    @PostMapping(version = "1")
    public UserResponseV1 create(@Valid @RequestBody CreateUserRequest req) {
        UserEntity u = service.create(req.name(), req.email());
        return new UserResponseV1(u.getId(), u.getName(), u.getEmail());
    }

    @GetMapping(value = "/{id}", version = "1")
    public UserResponseV1 get(@PathVariable("id") Long id) {
        UserEntity u = service.get(id);
        return new UserResponseV1(u.getId(), u.getName(), u.getEmail());
    }
}
