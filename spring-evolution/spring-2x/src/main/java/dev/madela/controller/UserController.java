package dev.madela.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.madela.model.User;
import dev.madela.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private UserService userService;

    // Setter Injection — основной DI в Spring 2.x
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user.htm", method = RequestMethod.POST)
    public void createUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        if (name == null || email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing name or email");
            return;
        }

        User user = userService.createUser(name, email);

        response.setContentType("application/json");
        response.getWriter().write(
                "{ \"id\": " + user.getId() +
                        ", \"name\": \"" + user.getName() +
                        "\", \"email\": \"" + user.getEmail() + "\" }"
        );
    }

    @RequestMapping(value = "/user.htm", method = RequestMethod.GET)
    public void getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing id");
            return;
        }

        Long id = Long.parseLong(idParam);
        User user = userService.getUser(id);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }

        response.setContentType("application/json");
        response.getWriter().write(
                "{ \"id\": " + user.getId() +
                        ", \"name\": \"" + user.getName() +
                        "\", \"email\": \"" + user.getEmail() + "\" }"
        );
    }
}
