package org.madela;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PermutationsBacktrackingTest {

    private Set<List<Integer>> toSetOfLists(List<List<Integer>> lists) {
        return new HashSet<>(lists);
    }

    @Test
    @DisplayName("Перестановки: [1,2,3]")
    void testPermute_123() {
        PermutationsBacktracking solver = new PermutationsBacktracking();

        int[] nums = {1, 2, 3};
        List<List<Integer>> actual = solver.permute(nums);

        Set<List<Integer>> actualSet = toSetOfLists(actual);
        Set<List<Integer>> expectedSet = toSetOfLists(Arrays.asList(
                Arrays.asList(1,2,3),
                Arrays.asList(1,3,2),
                Arrays.asList(2,1,3),
                Arrays.asList(2,3,1),
                Arrays.asList(3,1,2),
                Arrays.asList(3,2,1)
        ));

        assertEquals(expectedSet.size(), actualSet.size());
        assertTrue(actualSet.containsAll(expectedSet));
    }

    @Test
    @DisplayName("Перестановки: один элемент")
    void testPermute_single() {
        PermutationsBacktracking solver = new PermutationsBacktracking();
        int[] nums = {42};
        List<List<Integer>> actual = solver.permute(nums);
        assertEquals(Collections.singletonList(Collections.singletonList(42)), actual);
    }

    @Test
    @DisplayName("Перестановки: пустой массив → пустая перестановка")
    void testPermute_empty() {
        PermutationsBacktracking solver = new PermutationsBacktracking();
        int[] nums = {};
        List<List<Integer>> actual = solver.permute(nums);
        assertEquals(1, actual.size());
        assertEquals(Collections.emptyList(), actual.get(0));
    }

}
