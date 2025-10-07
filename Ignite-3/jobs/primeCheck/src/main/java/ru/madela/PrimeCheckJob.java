package ru.madela;

import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.JobExecutionContext;

import java.util.concurrent.CompletableFuture;

public class PrimeCheckJob implements ComputeJob<Integer, Boolean> {

    @Override
    public CompletableFuture<Boolean> executeAsync(JobExecutionContext jobExecutionContext, Integer integer) {
        if (integer == null) {
            return CompletableFuture.completedFuture(false);
        }

        final boolean result = isPrime(integer);
        return CompletableFuture.completedFuture(result);
    }

    private static boolean isPrime(int n) {
        if (n <= 1)
            return false;
        if (n <= 3)
            return true;
        if (n % 2 == 0 || n % 3 == 0)
            return false;
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }

}
