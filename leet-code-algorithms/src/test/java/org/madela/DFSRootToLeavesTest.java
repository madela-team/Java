package org.madela;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DFSRootToLeavesTest {

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
    @DisplayName("DFS: пути от корня к листьям — базовый пример")
    void testBinaryTreePaths_basic() {
        TreeNode root = buildTree(1, 2, 3, null, 5);
        DFSRootToLeaves solver = new DFSRootToLeaves();

        List<String> actual = solver.binaryTreePaths(root);
        Set<String> expected = new HashSet<>(Arrays.asList("1->2->5", "1->3"));

        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
        assertTrue(new HashSet<>(actual).containsAll(expected));
    }

    @Test
    @DisplayName("DFS: пустое дерево → пустой список")
    void testBinaryTreePaths_empty() {
        DFSRootToLeaves solver = new DFSRootToLeaves();
        assertTrue(solver.binaryTreePaths(null).isEmpty());
    }

    @Test
    @DisplayName("DFS: дерево из одного узла")
    void testBinaryTreePaths_singleNode() {
        TreeNode root = buildTree(42);
        DFSRootToLeaves solver = new DFSRootToLeaves();
        assertEquals(Collections.singletonList("42"), solver.binaryTreePaths(root));
    }

}
