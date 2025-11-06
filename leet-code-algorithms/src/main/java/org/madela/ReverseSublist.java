package dev.madela;

public class ReverseSublist {

    public static void main(String[] args) {
        // Создаём список: 1 -> 2 -> 3 -> 4 -> 5
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);

        System.out.print("Исходный список: ");
        printList(head);

        ListNode newHead = reverseBetween(head, 2, 4);

        System.out.print("После разворота с 2 по 4: ");
        printList(newHead); // Ожидаем: 1 4 3 2 5
    }

    /**
     * Разворачивает подсписок с позиции m по n (1-индексированные).
     *
     * @param head голова исходного списка
     * @param m    начало подсписка (включительно)
     * @param n    конец подсписка (включительно)
     * @return новая голова списка после разворота
     */
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null || m == n) {
            return head;
        }

        // Фиктивный узел для упрощения обработки случая, когда m = 1
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // 1. Найти узел перед началом разворота (позиция m-1)
        ListNode prev = dummy;
        for (int i = 1; i < m; i++) {
            prev = prev.next;
        }
        
        // 2. Начало подсписка, который будем разворачивать
        ListNode current = prev.next;
        ListNode next;
        
        // 3. Разворачиваем (n - m) раз, используя классический метод разворота
        for (int i = 0; i < n - m; i++) {
            next = current.next;
            current.next = next.next;
            next.next = prev.next;
            prev.next = next;
        }
        
        return dummy.next;
    }

    // Вспомогательный метод для печати списка
    public static void printList(ListNode head) {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.val + " ");
            curr = curr.next;
        }
        System.out.println();
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) { this.val = val; }
    }
}