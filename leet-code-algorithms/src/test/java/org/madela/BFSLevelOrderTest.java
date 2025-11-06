package org.madela;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BFSLevelOrderTest {

    private TreeNode buildTree(Integer... arr) {
        if (arr.length == 0 || arr[0] == null)
            return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> q = new ArrayDeque<>();
        q.add(root);
        int i = 1;
        while (!q.isEmpty() && i < arr.length) {
            TreeNode cur = q.poll();
            if (arr[i] != null) {
                cur.left = new TreeNode(arr[i]);
                q.add(cur.left);
            }
            i++;
            if (i < arr.length && arr[i] != null) {
                cur.right = new TreeNode(arr[i]);
                q.add(cur.right);
            }
            i++;
        }
        return root;
    }

    @Test
    @DisplayName("BFS: пример из условия")
    void testLevelOrder_basic() {
        TreeNode root = buildTree(3, 9, 20, null, null, 15, 7);
        BFSLevelOrder solver = new BFSLevelOrder();

        List<List<Integer>> actual = solver.levelOrder(root);
        List<List<Integer>> expected = Arrays.asList(
                Collections.singletonList(3),
                Arrays.asList(9, 20),
                Arrays.asList(15, 7)
        );
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("BFS: пустое дерево")
    void testLevelOrder_empty() {
        BFSLevelOrder solver = new BFSLevelOrder();
        assertTrue(solver.levelOrder(null).isEmpty());
    }

    @Test
    @DisplayName("BFS: один узел")
    void testLevelOrder_single() {
        TreeNode root = buildTree(1);
        BFSLevelOrder solver = new BFSLevelOrder();
        assertEquals(
                Collections.singletonList(Collections.singletonList(1)),
                solver.levelOrder(root)
        );
    }

}
