package dev.madela;

import java.util.ArrayList;
import java.util.List;

public class BinaryTreeTraversal {

    public static void main(String[] args) {
        // Создаём дерево: [1, null, 2, 3]
        //     1
        //      \
        //       2
        //      /
        //     3
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(3);

        List<Integer> result = inorderTraversal(root);
        System.out.println("InOrder обход: " + result); // Выведет: [1, 3, 2]
    }

    /**
     * InOrder обход (лево → корень → право) — рекурсивная реализация.
     *
     * @param root корень дерева
     * @return список значений узлов в порядке InOrder
     */
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    /**
     * Вспомогательный рекурсивный метод для InOrder-обхода.
     *
     * @param node   текущий узел
     * @param result список, в который добавляются значения
     */
    private static void inorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }

        // Рекурсивно обходим левое поддерево
        inorderHelper(node.left, result);

        // Добавляем значение текущего узла (корня поддерева)
        result.add(node.val);

        // Рекурсивно обходим правое поддерево
        inorderHelper(node.right, result);
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }

    }
}