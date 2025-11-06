package dev.madela;

public class LinkedListCycle {

    public static void main(String[] args) {
        // Создаём список: 3 -> 2 -> 0 -> -4 -> (циклически указывает на 2)
        ListNode node1 = new ListNode(3);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(0);
        ListNode node4 = new ListNode(-4);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node2; // создаём цикл

        boolean result = hasCycle(node1);
        System.out.println("Список содержит цикл: " + result); // true
    }

    /**
     * Определяет, содержит ли связный список цикл.
     *
     * @param head голова связного списка
     * @return true, если цикл существует, иначе false
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }

        ListNode slow = head;        // Черепаха: 1 шаг за итерацию
        ListNode fast = head;        // Заяц: 2 шага за итерацию

        while (fast != null && fast.next != null) {
            slow = slow.next;              // +1 шаг
            fast = fast.next.next;         // +2 шага

            if (slow == fast) {
                return true; // Цикл обнаружен
            }
        }

        return false; // Цикла нет — заяц достиг конца
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }
}