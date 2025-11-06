package org.madela;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FibonacciDPTest {

    @Test
    @DisplayName("Фибоначчи: базовые значения")
    void testFib_baseCases() {
        FibonacciDP solver = new FibonacciDP();
        assertEquals(0, solver.fib(0));
        assertEquals(1, solver.fib(1));
    }

    @Test
    @DisplayName("Фибоначчи: n=5 → 5 (0,1,1,2,3,5)")
    void testFib_n5() {
        FibonacciDP solver = new FibonacciDP();
        assertEquals(5, solver.fib(5));
    }

    @Test
    @DisplayName("Фибоначчи: сверка с DP-массивом")
    void testFib_vsArrayDP() {
        FibonacciDP solver = new FibonacciDP();
        for (int n = 0; n <= 30; n++) {
            assertEquals(solver.fibDPArray(n), solver.fib(n), "Mismatch at n=" + n);
        }
    }

}
