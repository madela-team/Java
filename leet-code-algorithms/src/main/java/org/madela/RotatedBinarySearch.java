package dev.madela;

public class RotatedBinarySearch {

    public static void main(String[] args) {
        int[] nums = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;
        int result = search(nums, target);
        System.out.println("Индекс элемента " + target + ": " + result);
        // Ожидаемый результат: 4
    }

    /**
     * Метод для поиска индекса элемента target в повёрнутом отсортированном массиве.
     *
     * @param nums   повёрнутый отсортированный массив целых чисел (без дубликатов)
     * @param target искомое значение
     * @return индекс элемента, если найден; иначе -1
     */
    public static int search(int[] nums, int target) {
        // Инициализируем границы поиска
        int left = 0;
        int right = nums.length - 1;

        // Пока границы не пересеклись
        while (left <= right) {
            // Находим середину
            int mid = left + (right - left) / 2;

            // Если нашли искомый элемент — возвращаем его индекс
            if (nums[mid] == target) {
                return mid;
            }

            // Определяем, какая половина отсортирована:
            // Если левая половина отсортирована (включая mid)
            if (nums[left] <= nums[mid]) {
                // Проверяем, находится ли target в пределах отсортированной левой половины
                if (nums[left] <= target && target < nums[mid]) {
                    // Ищем в левой половине
                    right = mid - 1;
                } else {
                    // Иначе — ищем в правой половине
                    left = mid + 1;
                }
            } else {
                // Правая половина отсортирована
                // Проверяем, находится ли target в пределах отсортированной правой половины
                if (nums[mid] < target && target <= nums[right]) {
                    // Ищем в правой половине
                    left = mid + 1;
                } else {
                    // Иначе — ищем в левой половине
                    right = mid - 1;
                }
            }
        }

        // Элемент не найден
        return -1;
    }
}