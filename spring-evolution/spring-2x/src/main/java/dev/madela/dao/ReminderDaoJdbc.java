package dev.madela.dao;

import dev.madela.model.Reminder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ReminderDaoJdbc implements ReminderDao {

    private SimpleJdbcTemplate simpleJdbcTemplate;

    public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
        this.simpleJdbcTemplate = simpleJdbcTemplate;
    }

    private ParameterizedRowMapper<Reminder> reminderRowMapper =
            new ParameterizedRowMapper<Reminder>() {
                public Reminder mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Reminder r = new Reminder();
                    r.setId(rs.getLong("id"));
                    r.setUserId(rs.getLong("user_id"));
                    r.setText(rs.getString("text"));
                    r.setRemindAt(rs.getTimestamp("remind_at"));
                    return r;
                }
            };

    public Reminder create(Long userId, String text, Date remindAt) {

        Long id = simpleJdbcTemplate.queryForLong(
                "INSERT INTO reminders(user_id, text, remind_at) VALUES (?, ?, ?) RETURNING id",
                userId,
                text,
                new java.sql.Timestamp(remindAt.getTime())
        );

        Reminder r = new Reminder();
        r.setId(id);
        r.setUserId(userId);
        r.setText(text);
        r.setRemindAt(remindAt);

        return r;
    }

    public List<Reminder> findByUserId(Long userId) {
        return simpleJdbcTemplate.query(
                "SELECT id, user_id, text, remind_at FROM reminders WHERE user_id = ?",
                reminderRowMapper,
                userId
        );
    }
}
