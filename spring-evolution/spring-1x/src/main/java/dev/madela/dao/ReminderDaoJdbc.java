package dev.madela.dao;

import dev.madela.model.Reminder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ReminderDaoJdbc implements ReminderDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    public Reminder findById(Long id) {
        return (Reminder) jdbcTemplate.queryForObject(
                "SELECT id, user_id, text, remind_at FROM reminders WHERE id=?",
                new Object[]{id},
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Reminder r = new Reminder();
                        r.setId(rs.getLong("id"));
                        r.setUserId(rs.getLong("user_id"));
                        r.setText(rs.getString("text"));
                        r.setRemindAt(Timestamp.valueOf(rs.getString("remind_at")));
                        return r;
                    }
                }
        );
    }

    public Reminder create(Reminder r) {
        jdbcTemplate.update(
                "INSERT INTO reminders(user_id, text, remind_at) VALUES (?, ?, ?)",
                new Object[]{r.getUserId(), r.getText(), r.getRemindAt()}
        );
        return r;
    }

    public List findAllByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT id, user_id, text, remind_at FROM reminders WHERE user_id=?",
                new Object[]{userId},
                new RowMapper() {
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Reminder r = new Reminder();
                        r.setId(rs.getLong("id"));
                        r.setUserId(rs.getLong("user_id"));
                        r.setText(rs.getString("text"));
                        r.setRemindAt(Timestamp.valueOf(rs.getString("remind_at")));
                        return r;
                    }
                }
        );
    }
}
