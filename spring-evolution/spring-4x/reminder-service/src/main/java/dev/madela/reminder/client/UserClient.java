package dev.madela.reminder.client;

import dev.madela.reminder.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserClient {

    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDto getUser(Long userId) {
        return restTemplate.getForObject(
                "http://USER-SERVICE/api/users/{id}",
                UserDto.class,
                userId
        );
    }
}
