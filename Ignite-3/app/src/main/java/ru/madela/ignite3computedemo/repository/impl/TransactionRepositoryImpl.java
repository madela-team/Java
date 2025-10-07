package ru.madela.ignite3computedemo.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.madela.ignite3computedemo.model.TransactionModel;
import ru.madela.ignite3computedemo.repository.TransactionRepository;
import ru.madela.ignite3computedemo.repository.mapper.TransactionRowMapper;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JdbcTemplate jdbc;
    private final TransactionRowMapper  mapper = new TransactionRowMapper();

    private static final String FIND_BY_ACCOUNT_ID = "SELECT * FROM tx WHERE account_id = ?";
    private static final String FIND_ALL = "SELECT * FROM tx";

    @Override
    public List<TransactionModel> findByAccountId(UUID id) {
        return jdbc.query(FIND_BY_ACCOUNT_ID, mapper, id);
    }

    @Override
    public List<TransactionModel> findAll() {
        return jdbc.query(FIND_ALL, mapper);
    }
}
