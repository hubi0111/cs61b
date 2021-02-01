package deque;

public class ArrayDeque<T> {

    private T[] deque;
    private int size;
    private int newFirst;
    private int newLast;
    private int cap;
    private static int expandFact = 2;
    private static int minCap = 16;
    private static double minRatio = 0.25;
    private static int reduceFact = 2;

    public ArrayDeque() {
        cap=8;
        deque = (T[]) new Object[8];
        newFirst = cap-1;
        newLast = 0;
        size = 0;
    }

    /**
     * Resizes the array to the specified number.
     *
     * @param newSize Size to resize to.
     */
    private void resize(int newSize) {
        T[] newItems = (T[]) new Object[newSize];

        int currentFirst = plusOne(newFirst);
        int currentLast = minusOne(newLast);

        if (currentFirst < currentLast) {
            int length = currentLast - currentFirst + 1;
            System.arraycopy(deque, currentFirst, newItems, 0, length);
            newFirst = newSize - 1;
            newLast = length;
        } else {
            int lengthFirsts = cap  - currentFirst;
            int newCurrentFirst = newSize - lengthFirsts;
            int lengthLasts = newLast;
            System.arraycopy(deque, currentFirst, newItems, newCurrentFirst, lengthFirsts);
            System.arraycopy(deque, 0, newItems, 0, lengthLasts);
            newFirst = newSize - lengthFirsts - 1;
        }

        cap = newSize;
        deque = newItems;
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
     * Adds an item to the front of the Deque.
     *
     * @param item item to be added.
     */
    public void addFirst(T item) {
        if (size == cap) {
            resize(size * expandFact);
        }
        deque[newFirst] = item;
        newFirst = minusOne(newFirst);
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
        deque[newLast] = item;
        newLast = plusOne(newLast);
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
        int curIndex = plusOne(newFirst);
        while (curIndex != newLast) {
            System.out.print(deque[curIndex] + " ");
            curIndex = plusOne(curIndex);
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
        int nextIndex = plusOne(newFirst);
        T firstItem = deque[nextIndex];
        deque[nextIndex] = null;
        newFirst = nextIndex;
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
        int prevIndex = minusOne(newLast);
        T lastItem = deque[prevIndex];
        deque[prevIndex] = null;
        newLast = prevIndex;
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
        index=index+newFirst+1;
        if (index >= cap) {
            index -= cap;
        }
        return deque[index];
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.printDeque();
        ad.addFirst(1);
        ad.printDeque();
        ad.addFirst(2);
        ad.printDeque();
        ad.addLast(3);
        ad.printDeque();
        System.out.println(ad.get(0));
        System.out.println(ad.get(1));
        System.out.println(ad.get(2));
        ad.removeFirst();
        ad.printDeque();
    }

}
