package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {

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
    @Override
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
    @Override
    public void addLast(T item) {
        if (size == cap) {
            resize(size * expandFact);
        }
        deque[last] = item;
        last = plusOne(last);
        size += 1;
    }

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
        for (int i = plusOne(first); i < plusOne(first) + size; i++) {
            System.out.print(deque[i] + " ");
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
    @Override
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
    @Override
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
        if (o == null || !(o instanceof Deque)) {
            return false;
        }
        Deque toLLD = (Deque) o;
        if (size != ((Deque<T>) o).size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(((Deque<T>) o).get(i))) {
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

        ArrayDequeIterator() {
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

}
