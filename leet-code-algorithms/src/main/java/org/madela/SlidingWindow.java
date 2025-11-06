package dev.madela;

public class SlidingWindow {

    public static void main(String[] args) {
        int[] nums = {2, 1, 5, 1, 3, 2};
        int k = 3;
        int result = maxSumSubarray(nums, k);
        System.out.println("Максимальная сумма подмассива размера " + k + ": " + result);
        // Ожидаемый вывод: 9
    }

    /**
     * Находит максимальную сумму подмассива длины k.
     *
     * @param nums входной массив целых чисел
     * @param k    размер окна (подмассива)
     * @return максимальная сумма подмассива длины k
     */
    public static int maxSumSubarray(int[] nums, int k) {
        if (nums == null || k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("Некорректные входные данные");
        }

        // Вычисляем сумму первого окна
        int windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += nums[i];
        }

        int maxSum = windowSum;

        // Скользим окно: от индекса k до конца массива
        for (int i = k; i < nums.length; i++) {
            // Вычитаем элемент, покидающий окно (слева)
            // Добавляем новый элемент, входящий в окно (справа)
            windowSum = windowSum - nums[i - k] + nums[i];
            maxSum = Math.max(maxSum, windowSum);
        }

        return maxSum;
    }


}