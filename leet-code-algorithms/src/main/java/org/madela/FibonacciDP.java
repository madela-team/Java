package org.madela;

public class FibonacciDP {

    public long fib(int n) {
        if (n < 2)
            return n;
        long a = 0;
        long b = 1;
        for (int i = 2; i <= n; i++) {
            long c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    public long fibDPArray(int n) {
        if (n < 2)
            return n;
        long[] dp = new long[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++)
            dp[i] = dp[i-1] + dp[i-2];
        return dp[n];
    }

}
