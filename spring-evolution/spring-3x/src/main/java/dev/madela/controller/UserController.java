package dev.madela.controller;

import dev.madela.dto.UserDto;
import dev.madela.model.User;
import dev.madela.service.UserService;
import dev.madela.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    // setter injection
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public UserDto createUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            HttpServletResponse response) throws IOException {

        if (name == null || email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing name or email");
            return null;
        }

        // Убедитесь, что userService не null перед вызовом
        if (userService == null) {
            throw new IllegalStateException("UserService was not injected!");
        }

        User user = userService.createUser(name, email);
        return UserMapper.toDto(user);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public UserDto getUser(
            @RequestParam("id") Long id,
            HttpServletResponse response) throws IOException {

        if (userService == null) {
            throw new IllegalStateException("UserService was not injected!");
        }

        User user = userService.getUser(id);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return null;
        }

        return UserMapper.toDto(user);
    }
}