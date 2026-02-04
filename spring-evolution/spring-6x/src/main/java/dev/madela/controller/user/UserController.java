package dev.madela.controller.user;

import dev.madela.controller.user.dto.CreateUserRequest;
import dev.madela.controller.user.dto.UserResponse;
import dev.madela.user.entity.UserEntity;
import dev.madela.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest req) {
        UserEntity u = service.create(req.name(), req.email());
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable("id") Long id) {
        UserEntity u = service.get(id);
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }
}
