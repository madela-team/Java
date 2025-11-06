package dev.madela;

import java.util.*;

public class MergeIntervals {

    public static void main(String[] args) {
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        
        int[][] merged = merge(intervals);
        
        System.out.println("Вход:  " + Arrays.deepToString(intervals));
        System.out.println("Выход: " + Arrays.deepToString(merged)); 
        // Ожидаем: [[1, 6], [8, 10], [15, 18]]
    }

    /**
     * Объединяет все перекрывающиеся или смежные интервалы.
     * Алгоритм:
     * Сортируем интервалы по начальной точке (start).
     * Инициализируем список для хранения объединённых интервалов.
     * Проходим по отсортированным интервалам:
     *     Если текущий интервал НЕ перекрывается с последним в результате
     *      (его start > end последнего), добавляем его как новый.
     *     Иначе — объединяем: обновляем end последнего интервала до max(старый end, текущий end).
     * 
     * @param intervals массив интервалов, где каждый интервал — это [start, end]
     * @return массив объединённых интервалов без перекрытий
     */
    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new int[0][];
        }

        // Сортируем интервалы по начальной точке (start)
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        // Создаём список для объединённых интервалов
        List<int[]> merged = new ArrayList<>();

        merged.add(intervals[0]);

        // Проходим по остальным интервалам
        for (int i = 1; i < intervals.length; i++) {
            int[] current = intervals[i];
            int[] lastMerged = merged.get(merged.size() - 1);

            // Если начало текущего интервала <= концу последнего объединённого — есть перекрытие (или они смежные)
            if (current[0] <= lastMerged[1]) {
                // Объединяем: расширяем конец последнего интервала до максимума
                lastMerged[1] = Math.max(lastMerged[1], current[1]);
            } else {
                // Нет перекрытия — добавляем текущий интервал как новый
                merged.add(current);
            }
        }

        return merged.toArray(new int[merged.size()][]);
    }
}