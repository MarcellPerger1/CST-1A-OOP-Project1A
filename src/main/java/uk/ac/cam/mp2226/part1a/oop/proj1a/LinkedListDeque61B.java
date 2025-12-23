package uk.ac.cam.mp2226.part1a.oop.proj1a;

import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    // The tests want this as a non-generic non-static class. I refuse.
    // Firstly, tests should NEVER EVER test the internal structure
    // in that type of detail ("if you have a problem and solve it using
    // reflection, you now have two problems").
    // Secondly, having a non-static inner class makes it less efficient as
    // each Node now holds an extra unnecessary reference to the containing
    // class and the containing class must be provided when creating a Node
    // which is super unnecessary. It also creates more reference loops
    // than there already are, decreasing performance due to GC.
    // Also, a Node shouldn't need to care where in the list it is
    // and shouldn't need to access the first item for example.
    private static class Node<T> {
        // This is class private and very intertwined with the containing
        // class so I wholeheartedly refuse to make these private and add
        // getters and setters. It's just extra unnecessary boilerplate!
        T value;
        Node<T> prev;
        Node<T> next;

        public Node(T value_, Node<T> prev_, Node<T> next_) {
            value = value_;
            prev = prev_;
            next = next_;
        }

        /// Insert new node with value `value` after `a`, before `b`
        public static <T> Node<T> newBetween(T value, Node<T> a, Node<T> b) {
            Node<T> self = new Node<>(value, a, b);
            a.next = b.prev = self;
            return self;
        }
        /// Remove the node from between its neighbours
        public Node<T> removeSelf() {
            prev.next = next;
            next.prev = prev;
            next = prev = null;  // Not necessary but nice for this to be correct
            return this;
        }
    }

    private final Node<T> sentinel;
    private int size;

    public LinkedListDeque61B() {
        sentinel = new Node<>(null, null, null);
        sentinel.next = sentinel.prev = sentinel;  // Close the loop
        size = 0;
    }

    private Node<T> first() {
        return sentinel.next;
    }
    private Node<T> last() {
        return sentinel.prev;
    }

    // Wow, this sentinel approach makes it so much simpler - no special cases!
    @Override
    public void addFirst(T x) {
        Node.newBetween(x, sentinel, first());
        size += 1;
    }
    @Override
    public void addLast(T x) {
        Node.newBetween(x, last(), sentinel);
        size += 1;
    }

    @Override
    public List<T> toList() {
        List<T> ls = new ArrayList<>(size);
        Node<T> curr = first();
        for (int i = 0; i < size; i++) {
            ls.add(curr.value);
            curr = curr.next;
        }
        return ls;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        size -= 1;
        return first().removeSelf().value;
    }
    @Override
    public T removeLast() {
        size -= 1;
        return last().removeSelf().value;
    }

    @Override
    public T get(int index) {
        Node<T> curr = first();
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.value;
    }

    @Override
    public T getRecursive(int index) {
        return getRecursive(first(), index);
    }
    private T getRecursive(Node<T> curr, int advance) {
        return advance == 0 ? curr.value : getRecursive(curr, advance - 1);
    }
}
