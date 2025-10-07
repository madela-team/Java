package ru.madela.ignite3computedemo.repository;

import ru.madela.ignite3computedemo.model.TransactionModel;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

    List<TransactionModel> findByAccountId(UUID id);

    List<TransactionModel> findAll();


}
