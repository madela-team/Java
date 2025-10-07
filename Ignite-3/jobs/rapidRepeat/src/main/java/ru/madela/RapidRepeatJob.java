package ru.madela;

import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.JobExecutionContext;
import org.apache.ignite.marshalling.ByteArrayMarshaller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RapidRepeatJob implements ComputeJob<UUID, RapidRepeatJobResponse> {

    private static final int WINDOW_MIN = 10;
    private static final long REPEAT_SEC = 30;

    @Override
    public CompletableFuture<RapidRepeatJobResponse> executeAsync(JobExecutionContext context, UUID accountId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minus(Duration.ofMinutes(WINDOW_MIN));
        Set<UUID> suspiciousIds = new LinkedHashSet<>();
        var ses = context.ignite().sql();
        try (var rs = ses.execute(
                null,
                "SELECT id, dt " +
                        "FROM tx " +
                        "WHERE account_id = ? " +
                        "AND dt BETWEEN ? AND ? " +
                        "ORDER BY dt",
                accountId,
                from,
                now
        )) {
            LocalDateTime prevDt = null;
            UUID prevId = null;
            while (rs.hasNext()) {
                var row = rs.next();
                LocalDateTime curDt = row.datetimeValue("dt");
                UUID curId = row.uuidValue("id");
                if (prevDt != null && Duration.between(prevDt, curDt).getSeconds() < REPEAT_SEC) {
                    suspiciousIds.add(prevId);
                    suspiciousIds.add(curId);
                }
                prevDt = curDt;
                prevId = curId;
            }
        }

        RapidRepeatJobResponse resp = new RapidRepeatJobResponse();
        resp.setAccountId(accountId);
        resp.setTransactionIdList(suspiciousIds);
        return CompletableFuture.completedFuture(resp);
    }

    @Override
    public ByteArrayMarshaller<RapidRepeatJobResponse> resultMarshaller() {
        return new RapidRepeatResponseMarshaller();
    }

}
