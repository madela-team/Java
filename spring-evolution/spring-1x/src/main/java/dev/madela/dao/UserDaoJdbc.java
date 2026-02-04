package dev.madela.dao;

import dev.madela.model.User;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    public User findById(Long id) {
        return (User) jdbcTemplate.queryForObject(
                "SELECT id, name, email FROM users WHERE id=?",
                new Object[]{id},
                new org.springframework.jdbc.core.RowMapper() {
                    public Object mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                        User u = new User();
                        u.setId(rs.getLong("id"));
                        u.setName(rs.getString("name"));
                        u.setEmail(rs.getString("email"));
                        return u;
                    }
                }
        );
    }

    public void create(User user) {
        jdbcTemplate.update(
                "INSERT INTO users(name, email) VALUES (?, ?)",
                new Object[]{user.getName(), user.getEmail()}
        );
    }
}
