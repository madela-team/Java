package dev.madela;

import java.util.PriorityQueue;

public class TopKElements {

    public static void main(String[] args) {
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        int result = findKthLargest(nums, k);
        System.out.println("K-й по величине элемент (k=" + k + "): " + result); // Ожидаем: 5
    }

    /**
     * Находит K-й по величине элемент в массиве.
     * Подход:
     * Используем минимальную кучу (min-heap), в которой всегда хранятся K наибольших элементов.
     * При добавлении нового элемента:
     *      Если куча ещё не заполнена (размер < k) — добавляем.
     *      Если новый элемент больше корня кучи (минимума среди топ-K), заменяем корень.
     * В конце корень кучи — это K-й по величине элемент.
     * 
     * @param nums входной неотсортированный массив
     * @param k    порядковый номер (1-индексированный): 1 — максимум, 2 — второй максимум и т.д.
     * @return K-й по величине элемент
     * @throws IllegalArgumentException если k <= 0 или k > nums.length
     */
    public static int findKthLargest(int[] nums, int k) {
        if (k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("k должно быть в диапазоне [1, длина массива]");
        }

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int num : nums) {
            if (minHeap.size() < k) {
                // Если куча ещё не заполнена до размера k — добавляем элемент
                minHeap.offer(num);
            } else if (num > minHeap.peek()) {
                // Если текущий элемент больше минимального среди топ-K, удаляем минимальный и добавляем текущий
                minHeap.poll();
                minHeap.offer(num);
            }
        }

        return minHeap.peek();
    }
}