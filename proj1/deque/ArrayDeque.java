package deque;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T> {

    private T[] deque;
    private int size;
    private int cap;
    private int first;
    private int last;
    private static int expandFact = 2;
    private static int minCap = 16;
    private static double minRatio = 0.25;
    private static int reduceFact = 2;

    public ArrayDeque() {
        cap = 8;
        deque = (T[]) new Object[8];
        first = cap - 1;
        last = 0;
        size = 0;

    }

    /**
     * Inceases the index by 1 if index is not the Deque's size.
     *
     * @param index index to increase.
     * @return
     */
    private int plusOne(int index) {
        if (index == cap - 1) {
            return 0;
        } else {
            return index + 1;
        }
    }

    /**
     * Reduces the index by 1 if index is not 0.
     *
     * @param index index to reduce.
     * @return
     */
    private int minusOne(int index) {
        if (index == 0) {
            return cap - 1;
        } else {
            return index - 1;
        }
    }

    /**
     * Resizes the array to the specified number.
     *
     * @param newSize Size to resize to.
     */
    private void resize(int newSize) {
        T[] newItems = (T[]) new Object[newSize];

        int currentFirst = plusOne(first);
        int currentLast = minusOne(last);

        if (currentFirst < currentLast) {
            int length = currentLast - currentFirst + 1;
            System.arraycopy(deque, currentFirst, newItems, 0, length);
            first = newSize - 1;
            last = length;
        } else {
            int lengthFirsts = cap - currentFirst;
            int newCurrentFirst = newSize - lengthFirsts;
            int lengthLasts = last;
            System.arraycopy(deque, currentFirst, newItems, newCurrentFirst, lengthFirsts);
            System.arraycopy(deque, 0, newItems, 0, lengthLasts);
            first = newSize - lengthFirsts - 1;
        }

        cap = newSize;
        deque = newItems;
    }

    /**
     * Adds an item to the front of the Deque.
     *
     * @param item item to be added.
     */
    public void addFirst(T item) {
        if (size == cap) {
            resize(size * expandFact);
        }
        deque[first] = item;
        first = minusOne(first);
        size += 1;
    }

    /**
     * Adds an item to the end of the Deque.
     *
     * @param item item to be added.
     */
    public void addLast(T item) {
        if (size == cap) {
            resize(size * expandFact);
        }
        deque[last] = item;
        last = plusOne(last);
        size += 1;
    }

    /**
     * Returns if the Deque is empty.
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
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
        int curIndex = plusOne(first);
        while (curIndex != last) {
            System.out.print(deque[curIndex] + " ");
            curIndex = curIndex + 1;
        }
        System.out.println();
    }

    /**
     * Removes the first element in Deque and returns it.
     *
     * @return
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int nextIndex = plusOne(first);
        T firstItem = deque[nextIndex];
        deque[nextIndex] = null;
        first = nextIndex;
        size -= 1;
        if (deque.length >= minCap && (double) size / deque.length < minRatio) {
            resize(deque.length / reduceFact);
        }
        return firstItem;
    }

    /**
     * Removes the last element in Deque and returns it.
     *
     * @return
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int prevIndex = minusOne(last);
        T lastItem = deque[prevIndex];
        deque[prevIndex] = null;
        last = prevIndex;
        size -= 1;
        if (deque.length >= minCap && (double) size / deque.length < minRatio) {
            resize(deque.length / reduceFact);
        }
        return lastItem;
    }

    /**
     * Returns the item at the specified index if it exists.
     *
     * @param index index wanted.
     * @return
     */
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        index = index + first + 1;
        if (index >= cap) {
            index -= cap;
        }
        return deque[index];
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    /**
     * Returns if all elements in o are equal to the elements in the current Deque.
     *
     * @param o LinkedListDeque to compare to
     * @return
     */
    public boolean equals(Object o) {
        ArrayDeque toLLD = (ArrayDeque) o;
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(((ArrayDeque<T>) o).get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Iterator class.
     * Retrieved from lecture 11 slides.
     */
    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;

        public ArrayDequeIterator() {
            wizPos = first + 1;
        }

        public boolean hasNext() {
            return wizPos < first + size + 1;
        }

        public T next() {
            T returnItem = deque[wizPos];
            wizPos += 1;
            return returnItem;
        }
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> LLD = new ArrayDeque<>();
        LLD.addFirst(1);
        LLD.addFirst(2);
        LLD.addFirst(3);
        LLD.addFirst(4);

        ArrayDeque<Integer>LLD2 = new ArrayDeque<>();
        LLD2.addFirst(1);
        LLD2.addFirst(2);
        LLD2.addFirst(3);
        LLD2.addFirst(4);
        System.out.println(LLD.equals(LLD2));
    }
}
