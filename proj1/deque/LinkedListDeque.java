package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {

    private class Node {
        T item;
        Node prev;
        Node next;

        private Node(T item, Node prev, Node next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    public Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /**
     * Adds an item to the front of the Deque.
     *
     * @param item item to be added.
     */
    @Override
    public void addFirst(T item) {
        Node newFirst = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newFirst;
        sentinel.next = newFirst;
        size += 1;
    }

    /**
     * Adds an item to the end of the Deque.
     *
     * @param item item to be added.
     */
    @Override
    public void addLast(T item) {
        Node oldLast = sentinel.prev;
        Node newLast = new Node(item, oldLast, sentinel);
        oldLast.next = newLast;
        sentinel.prev = newLast;
        size += 1;
    }

//    /**
//     * Returns if the Deque is empty.
//     *
//     * @return
//     */
//    @Override
//    public boolean isEmpty() {
//        return size == 0;
//    }

    /**
     * Returns the size of the Deque.
     *
     * @return
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Prints the items in the Deque.
     */
    @Override
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
    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        Node oldFirst = sentinel.next;
        Node newFirst = oldFirst.next;
        newFirst.prev = sentinel;
        sentinel.next = newFirst;
        size -= 1;
        return oldFirst.item;
    }

    /**
     * Removes the last element in Deque and returns it.
     *
     * @return
     */
    @Override
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }
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
    @Override
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

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    /**
     * Returns if all elements in o are equal to the elements in the current Deque.
     * @param o LinkedListDeque to compare to
     * @return
     */
    public boolean equals(Object o) {
        LinkedListDeque toLLD = (LinkedListDeque) o;
        for(int i = 0;i<size;i++){
            if(!get(i).equals(((LinkedListDeque<T>) o).get(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * Iterator class.
     * Retrieved from lecture 11 slides.
     */
    private class LinkedListDequeIterator implements Iterator<T> {
        private int wizPos;

        public LinkedListDequeIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public T next() {
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }
    }
    public static void main(String[]args){
        LinkedListDeque<Integer>LLD = new LinkedListDeque<>();
        LLD.addFirst(1);
        LLD.addFirst(2);
        LLD.addFirst(3);
        LLD.addFirst(4);

        LinkedListDeque<Integer>LLD2 = new LinkedListDeque<>();
        LLD2.addFirst(1);
        LLD2.addFirst(2);
        LLD2.addFirst(4);
        LLD2.addFirst(4);
        System.out.println(LLD.equals(LLD2));
    }
}
