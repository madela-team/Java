package dev.madela.controller;

import dev.madela.model.Reminder;
import dev.madela.service.ReminderService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ReminderController {

    private ReminderService reminderService;

    public void setReminderService(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    private static final SimpleDateFormat FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping(value = "/reminder.htm", method = RequestMethod.POST)
    public void createReminder(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String userIdParam = request.getParameter("userId");
        String text = request.getParameter("text");
        String remindAtStr = request.getParameter("remindAt");

        if (userIdParam == null || text == null || remindAtStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        Long userId = Long.valueOf(userIdParam);
        Date remindAt = FORMAT.parse(remindAtStr);

        Reminder r = reminderService.createReminder(userId, text, remindAt);

        response.setContentType("application/json");
        response.getWriter().write(
                "{ \"id\": " + r.getId() +
                        ", \"userId\": " + r.getUserId() +
                        ", \"text\": \"" + r.getText() +
                        "\", \"remindAt\": \"" + FORMAT.format(r.getRemindAt()) + "\" }"
        );
    }

    @RequestMapping(value = "/reminder.htm", method = RequestMethod.GET)
    public void getReminders(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String userIdParam = request.getParameter("userId");
        if (userIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userId");
            return;
        }

        Long userId = Long.valueOf(userIdParam);
        List<Reminder> reminders = reminderService.getByUserId(userId);

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < reminders.size(); i++) {
            Reminder r = reminders.get(i);

            sb.append("{")
                    .append("\"id\": ").append(r.getId()).append(",")
                    .append("\"userId\": ").append(r.getUserId()).append(",")
                    .append("\"text\": \"").append(r.getText()).append("\",")
                    .append("\"remindAt\": \"").append(FORMAT.format(r.getRemindAt())).append("\"")
                    .append("}");

            if (i < reminders.size() - 1) sb.append(",");
        }

        sb.append("]");

        response.setContentType("application/json");
        response.getWriter().write(sb.toString());
    }
}
