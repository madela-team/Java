package dev.madela;

public class TwoPointers {

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 6};
        int target = 6;

        int[] result = twoSum(nums, target);
        System.out.println("Индексы: [" + result[0] + ", " + result[1] + "]");
        // Ожидаемый вывод: [1, 3] → nums[1] = 2, nums[3] = 4 → 2 + 4 = 6
    }
    
    /**
     * Находит два индекса в отсортированном массиве,
     * сумма элементов по которым равна target.
     *
     * @param nums   отсортированный массив целых чисел
     * @param target целевая сумма
     * @return массив из двух индексов [i, j], где i < j и nums[i] + nums[j] == target
     */
    public static int[] twoSum(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int currentSum = nums[left] + nums[right];
            
            if (currentSum == target) {
                return new int[]{left, right};
            } else if (currentSum < target) {
                left++; // нужно увеличить сумму - взять большее число
            } else {
                right--; // нужно уменьшить сумму - взять меньшее число
            }
        }

        throw new IllegalArgumentException("Решения не найдено");
    }


}