package dev.madela.reminder.client;

import dev.madela.reminder.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserClient {

    private final WebClient userWebClient;

    public UserClient(WebClient userWebClient) {
        this.userWebClient = userWebClient;
    }

    public Mono<UserDto> getUser(Long id) {
        return userWebClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
}
