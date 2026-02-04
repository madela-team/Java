package dev.madela.user.dao;

import dev.madela.user.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoJdbc implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, email FROM users WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) ->
                        new User(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email")
                        )
        );
    }

    @Override
    public Long create(String name, String email) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO users(name, email) VALUES (?, ?) RETURNING id",
                Long.class,
                name,
                email
        );
    }
}
