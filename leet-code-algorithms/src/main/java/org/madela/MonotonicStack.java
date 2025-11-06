package dev.madela;

import java.util.Arrays;
import java.util.Stack;

public class MonotonicStack {

    public static void main(String[] args) {
        int[] nums = {2, 1, 2, 4, 3};
        int[] result = nextGreaterElement(nums);
        System.out.println("Вход:  " + Arrays.toString(nums));
        System.out.println("Выход: " + Arrays.toString(result)); // Ожидаем: [4, 2, 4, -1, -1]
    }

    /**
     * Находит следующий больший элемент для каждого элемента массива.
     * Алгоритм:
     *  Используем стек для хранения индексов элементов, для которых ещё не найден следующий больший.
     *  Стек поддерживается в монотонно убывающем порядке значений (чем глубже в стеке — тем больше значение).
     *  При встрече элемента, большего, чем элемент на вершине стека, мы "закрываем" этот элемент, его следующий больший — текущий элемент.
     * 
     * @param nums входной массив целых чисел
     * @return массив, где result[i] — следующий больший элемент для nums[i], или -1, если его нет
     */
    public static int[] nextGreaterElement(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        
        Arrays.fill(result, -1);
        
        // Стек хранит индексы элементов (не сами значения!), чтобы знать, куда записывать результат
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                // Извлекаем индекс из стека — для этого элемента мы нашли следующий больший
                int index = stack.pop();
                // Записываем текущий элемент как следующий больший для nums[index]
                result[index] = nums[i];
            }
            // Текущий индекс всегда помещается в стек — возможно, для него найдётся больший элемент позже
            stack.push(i);
        }

        return result;
    }
}