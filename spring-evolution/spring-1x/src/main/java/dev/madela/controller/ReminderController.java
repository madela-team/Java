package dev.madela.controller;

import dev.madela.model.Reminder;
import dev.madela.service.ReminderService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

public class ReminderController implements Controller {

    private ReminderService reminderService;

    public void setReminderService(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        String method = request.getMethod();

        // CREATE (POST)
        if ("POST".equalsIgnoreCase(method)) {

            Reminder r = new Reminder();
            r.setUserId(Long.valueOf(request.getParameter("userId")));
            r.setText(request.getParameter("text"));
            r.setRemindAt(Timestamp.valueOf(request.getParameter("remindAt")));

            reminderService.createReminder(r);

            response.setContentType("application/json");
            response.getWriter().write(
                    "{ \"status\":\"created\", \"userId\": " + r.getUserId() +
                            ", \"text\":\"" + r.getText() +
                            "\", \"remindAt\":\"" + r.getRemindAt() + "\" }"
            );
            return null;
        }

        // GET one (GET /reminder.htm?id=Х)
        if (request.getParameter("id") != null) {
            Long id = Long.valueOf(request.getParameter("id"));
            Reminder r = reminderService.getReminder(id);

            response.setContentType("application/json");
            response.getWriter().write(
                    "{ \"id\": " + r.getId() +
                            ", \"userId\": " + r.getUserId() +
                            ", \"text\": \"" + r.getText() +
                            "\", \"remindAt\": \"" + r.getRemindAt() + "\" }"
            );
            return null;
        }

        // GET list by User (GET /reminder.htm?userId=X)
        if (request.getParameter("userId") != null) {
            Long userId = Long.valueOf(request.getParameter("userId"));
            List reminders = reminderService.getUserReminders(userId);

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < reminders.size(); i++) {
                Reminder r = (Reminder) reminders.get(i);
                json.append("{")
                        .append("\"id\":").append(r.getId()).append(",")
                        .append("\"userId\":").append(r.getUserId()).append(",")
                        .append("\"text\":\"").append(r.getText()).append("\",")
                        .append("\"remindAt\":\"").append(r.getRemindAt()).append("\"")
                        .append("}");
                if (i < reminders.size() - 1) json.append(",");
            }
            json.append("]");

            response.setContentType("application/json");
            response.getWriter().write(json.toString());
            return null;
        }

        response.sendError(400, "Specify id or userId");
        return null;
    }
}
