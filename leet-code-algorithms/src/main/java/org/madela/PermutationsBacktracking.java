package org.madela;

import java.util.ArrayList;
import java.util.List;

public class PermutationsBacktracking {

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), res);
        return res;
        // Альтернатива: алгоритм Хипа или обмены in-place.
    }

    private void backtrack(int[] nums, boolean[] used, List<Integer> cur, List<List<Integer>> res) {
        if (cur.size() == nums.length) {
            res.add(new ArrayList<>(cur));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true;
            cur.add(nums[i]);
            backtrack(nums, used, cur, res);
            cur.remove(cur.size() - 1);
            used[i] = false;
        }
    }

}
