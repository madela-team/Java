package dev.madela.user.controller;

import dev.madela.user.dto.CreateUserRequest;
import dev.madela.user.dto.UserDto;
import dev.madela.user.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<UserDto> create(@RequestBody CreateUserRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public Mono<UserDto> get(@PathVariable Long id) {
        return service.findById(id);
    }
}
