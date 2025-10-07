package ru.madela.ignite3computedemo.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.compute.JobDescriptor;
import org.apache.ignite.compute.JobTarget;
import org.apache.ignite.deployment.DeploymentUnit;
import org.apache.ignite.table.Tuple;
import org.springframework.stereotype.Service;
import ru.madela.RapidRepeatJob;
import ru.madela.RapidRepeatJobResponse;
import ru.madela.RapidRepeatResponseMarshaller;
import ru.madela.ignite3computedemo.model.TransactionModel;
import ru.madela.ignite3computedemo.repository.TransactionRepository;
import ru.madela.ignite3computedemo.service.IgniteComputeService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IgniteComputeServiceImpl implements IgniteComputeService {

    private final IgniteClient igniteClient;
    private final TransactionRepository transactionRepository;

    @Override
    public Boolean primeCheck(Integer number) {

        JobDescriptor<Integer, Boolean> job = JobDescriptor.<Integer, Boolean>builder("ru.madela.PrimeCheckJob")
                .units(new DeploymentUnit("prime-check-job", "1.0.0"))
                .build();

        JobTarget jobTarget = JobTarget.anyNode(igniteClient.clusterNodes());
        return igniteClient.compute().execute(jobTarget, job, number);
    }

    public List<TransactionModel> findByAccountId(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Override
    public RapidRepeatJobResponse findRapidRepeats(UUID accountId) {
        JobDescriptor<UUID, RapidRepeatJobResponse> rapidRepeatJob =
                JobDescriptor.builder(RapidRepeatJob.class)
                        .units(List.of(new DeploymentUnit("rapid-repeat-job", "1.0.0")))
                        .resultClass(RapidRepeatJobResponse.class)
                        .resultMarshaller(new RapidRepeatResponseMarshaller())
                        .build();
        JobTarget target = JobTarget.colocated("tx", accountKey(accountId));
        return igniteClient.compute().execute(target, rapidRepeatJob, accountId);
    }

    @Override
    public List<RapidRepeatJobResponse> findAllAccountsRapidRepeats() {
        List<TransactionModel> all = transactionRepository.findAll();
        List<RapidRepeatJobResponse> accountIds = new ArrayList<>();
        all.forEach(t -> accountIds.add(findRapidRepeats(t.getAccountId())));
        return accountIds;
    }

    private Tuple accountKey(UUID accountId) {
        return Tuple.create()
                .set("ACCOUNT_ID", accountId)
                .set("id", new UUID(0L, 0L));
    }

}
