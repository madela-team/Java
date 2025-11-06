package org.madela;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FloodFillDFSTest {

    @Test
    @DisplayName("FloodFill: пример из условия")
    void testFloodFill_basic() {
        int[][] image = {
                {1,1,1},
                {1,1,0},
                {1,0,1}
        };
        int[][] expected = {
                {2,2,2},
                {2,2,0},
                {2,0,1}
        };
        FloodFillDFS solver = new FloodFillDFS();

        int[][] actual = solver.floodFill(image, 1, 1, 2);

        assertEquals(expected.length, actual.length);
        for (int r = 0; r < expected.length; r++) {
            assertArrayEquals(expected[r], actual[r], "Row " + r + " differs");
        }
    }

    @Test
    @DisplayName("FloodFill: если цвет совпадает — ничего не меняем")
    void testFloodFill_sameColorNoChange() {
        int[][] image = {
                {0,0},
                {0,1}
        };
        int[][] copy = {
                {0,0},
                {0,1}
        };
        FloodFillDFS solver = new FloodFillDFS();

        int[][] actual = solver.floodFill(image, 0, 0, 0);

        for (int r = 0; r < copy.length; r++) {
            assertArrayEquals(copy[r], actual[r]);
        }
    }

    @Test
    @DisplayName("FloodFill: изолированный пиксель")
    void testFloodFill_isolatedPixel() {
        int[][] image = {
                {1,1,1},
                {1,9,1},
                {1,1,1}
        };
        int[][] expected = {
                {1,1,1},
                {1,7,1},
                {1,1,1}
        };
        FloodFillDFS solver = new FloodFillDFS();

        int[][] actual = solver.floodFill(image, 1, 1, 7);
        for (int r = 0; r < expected.length; r++) {
            assertArrayEquals(expected[r], actual[r]);
        }
    }

}
