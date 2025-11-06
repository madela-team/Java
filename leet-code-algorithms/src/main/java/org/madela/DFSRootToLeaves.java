package org.madela;

import java.util.ArrayList;
import java.util.List;

public class DFSRootToLeaves {

    public List<String> binaryTreePaths(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null)
            return res;
        dfs(root, new StringBuilder(), res);
        return res;
    }

    private void dfs(TreeNode node, StringBuilder path, List<String> res) {
        int len = path.length();
        if (len > 0)
            path.append("->");
        path.append(node.val);

        if (node.left == null && node.right == null) {
            res.add(path.toString());
        } else {
            if (node.left  != null)
                dfs(node.left,  path, res);
            if (node.right != null)
                dfs(node.right, path, res);
        }
        path.setLength(len);
    }

}
