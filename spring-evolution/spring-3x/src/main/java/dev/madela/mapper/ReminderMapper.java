package dev.madela.mapper;

import dev.madela.dto.ReminderDto;
import dev.madela.model.Reminder;

import java.text.SimpleDateFormat;

public class ReminderMapper {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private ReminderMapper() {
    }

    public static ReminderDto toDto(Reminder reminder) {
        if (reminder == null) {
            return null;
        }

        String date = null;
        if (reminder.getRemindAt() != null) {
            date = new SimpleDateFormat(DATE_PATTERN)
                    .format(reminder.getRemindAt());
        }

        return new ReminderDto(
                reminder.getId(),
                UserMapper.toDto(reminder.getUser()),
                reminder.getText(),
                date
        );
    }
}
