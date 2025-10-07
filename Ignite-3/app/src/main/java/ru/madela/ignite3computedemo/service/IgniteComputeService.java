package ru.madela.ignite3computedemo.service;

import ru.madela.RapidRepeatJobResponse;
import ru.madela.ignite3computedemo.model.TransactionModel;

import java.util.List;
import java.util.UUID;

public interface IgniteComputeService {

    Boolean primeCheck(Integer number);
    List<TransactionModel> findByAccountId(UUID accountId);

    RapidRepeatJobResponse findRapidRepeats(UUID accountId);

    List<RapidRepeatJobResponse> findAllAccountsRapidRepeats();

}
