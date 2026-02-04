package dev.madela.dao;

import dev.madela.model.User;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoJdbc implements UserDao {

    private SimpleJdbcTemplate simpleJdbcTemplate;

    public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
        this.simpleJdbcTemplate = simpleJdbcTemplate;
    }

    private ParameterizedRowMapper<User> userRowMapper =
            new ParameterizedRowMapper<User>() {
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setName(rs.getString("name"));
                    u.setEmail(rs.getString("email"));
                    return u;
                }
            };

    public User findById(Long id) {
        return simpleJdbcTemplate.queryForObject(
                "SELECT id, name, email FROM users WHERE id = ?",
                userRowMapper,
                id
        );
    }

    public User create(String name, String email) {
        Long id = simpleJdbcTemplate.queryForLong(
                "INSERT INTO users(name, email) VALUES (?, ?) RETURNING id",
                name, email
        );

        return new User(id, name, email);
    }
}
