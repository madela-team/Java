package dev.madela.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dev.madela.model.User;
import dev.madela.service.UserService;
import org.springframework.web.servlet.mvc.Controller;

public class UserController implements Controller {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public org.springframework.web.servlet.ModelAndView handleRequest(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String method = request.getMethod();

        if ("POST".equalsIgnoreCase(method)) {
            return handleCreate(request, response);
        }

        if ("GET".equalsIgnoreCase(method)) {
            return handleGet(request, response);
        }

        response.sendError(405, "Method Not Allowed");
        return null;
    }

    private org.springframework.web.servlet.ModelAndView handleCreate(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String name = request.getParameter("name");
        String email = request.getParameter("email");

        if (name == null || email == null) {
            response.sendError(400, "Missing name or email");
            return null;
        }

        User user = userService.createUser(name, email);

        response.setContentType("application/json");
        response.getWriter().write(
                "{ \"id\": " + user.getId() +
                        ", \"name\": \"" + user.getName() +
                        "\", \"email\": \"" + user.getEmail() + "\" }"
        );
        return null;
    }

    private org.springframework.web.servlet.ModelAndView handleGet(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(400, "Missing id");
            return null;
        }

        Long id = Long.valueOf(idParam);
        User user = userService.getUser(id);

        if (user == null) {
            response.sendError(404, "User not found");
            return null;
        }

        response.setContentType("application/json");
        response.getWriter().write(
                "{ \"id\": " + user.getId() +
                        ", \"name\": \"" + user.getName() +
                        "\", \"email\": \"" + user.getEmail() + "\" }"
        );
        return null;
    }
}
