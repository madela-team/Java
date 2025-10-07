package ru.madela;

import java.util.Set;
import java.util.UUID;

public class RapidRepeatJobResponse {
    private UUID accountId;
    private Set<UUID> transactionIdList;

    public RapidRepeatJobResponse() {
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public Set<UUID> getTransactionIdList() {
        return transactionIdList;
    }

    public void setTransactionIdList(Set<UUID> transactionIdList) {
        this.transactionIdList = transactionIdList;
    }

    public UUID getAccountId() {
        return accountId;
    }
}
