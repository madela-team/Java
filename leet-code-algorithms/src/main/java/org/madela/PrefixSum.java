package dev.madela;

public class PrefixSum {

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6};
        int[] prefixSum = buildPrefix(nums);

        int i = 1, j = 3;
        int result = rangeSum(prefixSum, i, j); // ожидается 2 + 3 + 4 = 9
        System.out.println("Сумма от " + i + " до " + j + ": " + result);
    }

    /**
     * Строит префиксный массив: каждый элемент содержит сумму всех элементов до текущего включительно
     * @param nums исходный массив целых чисел
     * @return массив префиксных сумм той же длины, что и nums
     */
    public static int[] buildPrefix(int[] nums) {
        int[] prefix = new int[nums.length];
        if (nums.length == 0) return prefix;
        
        prefix[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            prefix[i] = prefix[i - 1] + nums[i];
        }
        return prefix;
    }

    /**
     * Вычисляет сумму элементов исходного массива в диапазоне [i, j] включительно, используя уже построенный префиксный массив.
     * @param prefix префиксный массив, полученный из buildPrefix
     * @param i левая граница диапазона
     * @param j правая граница диапазона
     * @return сумма элементов исходного массива от индекса i до j
     * @throws IllegalArgumentException если диапазон недопустим (выходит за границы или i > j)
     */
    public static int rangeSum(int[] prefix, int i, int j) {
        if (i < 0 || j >= prefix.length || i > j) {
            throw new IllegalArgumentException("Диапазон недопустим");
        }
        if (i == 0) {
            return prefix[j];
        }
        return prefix[j] - prefix[i - 1];
    }
}