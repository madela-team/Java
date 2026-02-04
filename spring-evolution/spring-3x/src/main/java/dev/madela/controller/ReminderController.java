package dev.madela.controller;

import dev.madela.dto.ReminderDto;
import dev.madela.mapper.ReminderMapper;
import dev.madela.model.Reminder;
import dev.madela.service.ReminderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/api/reminders")
public class ReminderController {

    private ReminderService reminderService;

    // setter injection — норма эпохи
    @Autowired
    public void setReminderService(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ReminderDto createReminder(
            @RequestParam("userId") Long userId,
            @RequestParam("text") String text,
            @RequestParam("remindAt") String remindAt,
            HttpServletResponse response) throws IOException {

        if (userId == null || text == null || remindAt == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return null;
        }

        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(remindAt);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
            return null;
        }

        // Убедитесь, что reminderService не null перед вызовом
        if (reminderService == null) {
            throw new IllegalStateException("ReminderService was not injected!");
        }

        Reminder reminder = reminderService.createReminder(userId, text, date);
        return ReminderMapper.toDto(reminder);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ReminderDto> getRemindersByUser(
            @RequestParam("userId") Long userId,
            HttpServletResponse response) throws IOException {

        if (reminderService == null) {
            throw new IllegalStateException("ReminderService was not injected!");
        }

        List<Reminder> reminders = reminderService.getByUserId(userId);
        List<ReminderDto> result = new ArrayList<ReminderDto>();

        for (Reminder r : reminders) {
            result.add(ReminderMapper.toDto(r));
        }

        return result;
    }
}