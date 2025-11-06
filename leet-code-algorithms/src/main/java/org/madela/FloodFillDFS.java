package org.madela;

public class FloodFillDFS {
    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int orig = image[sr][sc];
        if (orig == newColor)
            return image;
        dfs(image, sr, sc, orig, newColor);
        return image;
    }

    private void dfs(int[][] img, int r, int c, int orig, int color) {
        int m = img.length;
        int n = img[0].length;
        if (r < 0 || c < 0 || r >= m || c >= n || img[r][c] != orig)
            return;
        img[r][c] = color;
        dfs(img, r+1, c, orig, color);
        dfs(img, r-1, c, orig, color);
        dfs(img, r, c+1, orig, color);
        dfs(img, r, c-1, orig, color);
    }
}
