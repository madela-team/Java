package dev.madela.reminder.client;

import dev.madela.reminder.dto.UserDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/users")
public interface UserHttpClient {

    @GetExchange("/{id}")
    UserDto getUser(@PathVariable("id") Long id);
}
