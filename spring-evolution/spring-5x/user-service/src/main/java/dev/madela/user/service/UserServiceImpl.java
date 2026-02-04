package dev.madela.user.service;

import dev.madela.user.dto.CreateUserRequest;
import dev.madela.user.dto.UserDto;
import dev.madela.user.entity.User;
import dev.madela.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public Mono<UserDto> create(CreateUserRequest req) {
        if (req.getName() == null || req.getEmail() == null) {
            return Mono.error(new IllegalArgumentException("name/email required"));
        }

        return repo.findByEmail(req.getEmail())
                .flatMap(existing -> Mono.<UserDto>error(new IllegalArgumentException("email already exists")))
                .switchIfEmpty(
                        repo.save(new User(null, req.getName(), req.getEmail()))
                                .map(this::toDto)
                );
    }

    @Override
    public Mono<UserDto> findById(Long id) {
        return repo.findById(id).map(this::toDto);
    }

    private UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail());
    }
}
