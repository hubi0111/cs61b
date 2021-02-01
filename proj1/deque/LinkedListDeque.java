package deque;

public class LinkedListDeque<T> {

    private class Node {
        Node prev;
        T item;
        Node next;

        private Node(T item, Node prev, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        this.sentinel = new Node(null, null, null);
        this.sentinel.prev = this.sentinel;
        this.sentinel.next = this.sentinel;
        this.size = 0;
    }

    /**
     * Adds an item to the front of the Deque.
     *
     * @param item item to be added.
     */
    public void addFirst(T item) {
        Node newFirst = new Node(item, this.sentinel, this.sentinel.next);
        this.sentinel.next.prev = newFirst;
        this.sentinel.next = newFirst;
        this.size += 1;
    }

    /**
     * Adds an item to the end of the Deque.
     *
     * @param item item to be added.
     */
    public void addLast(T item) {
        Node oldLast = this.sentinel.prev;
        Node newLast = new Node(item, oldLast, this.sentinel);
        oldLast.next = newLast;
        this.sentinel.prev = newLast;
        this.size += 1;
    }

    /**
     * Returns if the Deque is empty.
     *
     * @return
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Returns the size of the Deque.
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Prints the items in the Deque.
     */
    public void printDeque() {
        Node cur = this.sentinel;
        while (cur.next != this.sentinel) {
            System.out.print(cur.next.item + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    /**
     * Removes the first element in Deque and returns it.
     *
     * @return
     */
    public T removeFirst() {
        Node oldFirst = this.sentinel.next;
        Node newFirst = oldFirst.next;
        newFirst.prev = this.sentinel;
        this.sentinel.next = newFirst;
        this.size -= 1;
        return oldFirst.item;
    }

    /**
     * Removes the last element in Deque and returns it.
     *
     * @return
     */
    public T removeLast() {
        Node oldLast = this.sentinel.prev;
        Node newLast = oldLast.prev;
        newLast.next = this.sentinel;
        this.sentinel.prev = newLast;
        this.size -= 1;
        return oldLast.item;
    }

    /**
     * Returns the item at the specified index if it exists.
     *
     * @param index index wanted.
     * @return
     */
    public T get(int index) {
        Node cur = this.sentinel;
        if (index >= this.size || index < 0) {
            return null;
        }
        while (index >= 0) {
            cur = cur.next;
            index -= 1;
        }
        return cur.item;
    }

    /**
     * Returns the item at the specified index if it exists.
     *
     * @param index index wanted
     * @return
     */
    public T getRecursive(int index) {
        if (index >= this.size || index < 0) {
            return null;
        }
        return getRecursiveHelper(this.sentinel, index);
    }

    /**
     * Helper Method for getRecursive.
     *
     * @param cur   current Node.
     * @param index index curently at.
     * @return
     */
    public T getRecursiveHelper(Node cur, int index) {
        if (index == 0) {
            return cur.next.item;
        } else {
            return getRecursive(index - 1);
        }
    }
}
