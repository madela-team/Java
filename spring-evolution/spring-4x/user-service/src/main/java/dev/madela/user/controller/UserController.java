package dev.madela.user.controller;

import dev.madela.user.dto.CreateUserRequest;
import dev.madela.user.model.User;
import dev.madela.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User create(@RequestBody CreateUserRequest req) {
        return service.create(req.getName(), req.getEmail());
    }

    @GetMapping("/{id}")
    public CompletableFuture<User> get(@PathVariable Long id) {
        return service.getUserAsync(id);
    }
}

