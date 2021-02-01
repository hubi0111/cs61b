package deque;

public class ArrayDeque<T> {

    private T[] deque;
    private int size;
    private int newFirst;
    private int newLast;
    private final double usageFactor = 0.25;

    public ArrayDeque() {
        this.deque = (T[]) new Object[8];
        this.size = 0;
        this.newFirst = 0;
        this.newLast = 1;
    }

    /**
     * Doubles the size of Deque.
     *
     * @param curSize current size of Deque.
     */
    private void doubleSize(int curSize) {
        T[] newDeque = (T[]) new Object[curSize * 2];
        for (int i = 0; i < this.size; i++) {
            newDeque[i] = this.get(i);
        }
        this.deque = newDeque;
        this.newFirst = this.deque.length - 1;
        this.newFirst = this.size;
    }

    /**
     * Adds an item to the front of the Deque.
     *
     * @param item item to be added.
     */
    public void addFirst(T item) {
        if (this.size == this.deque.length) {
            this.doubleSize(this.size);
        }
        this.deque[this.newFirst] = item;
        this.newFirst -= 1;
        this.size += 1;
    }

    /**
     * Adds an item to the end of the Deque.
     *
     * @param item item to be added.
     */
    public void addLast(T item) {
        if (this.size == this.deque.length) {
            this.doubleSize(this.size);
        }
        this.deque[this.newLast] = item;
        this.newLast += 1;
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
        return this.size;
    }

    /**
     * Prints the items in the Deque.
     */
    public void printDeque() {
        for (int i = 0; i < this.size; i++) {
            System.out.print(this.get(i) + " ");
        }
        System.out.println();
    }

    /**
     * Halves the size of Deque
     *
     * @param size current size of Deque.
     */
    private void halfSize(int size) {
        T[] newDeque = (T[]) new Object[size / 2];
        for (int i = 0; i < this.size; i++) {
            newDeque[i] = this.get(i);
        }
        this.deque = newDeque;
        this.newFirst = this.deque.length - 1;
        this.newLast = this.size;
    }

    /**
     * Checks and reduces the Deque size.
     */
    private void reduce() {
        double usageRatio = (double) this.size / this.deque.length;
        if (usageRatio < usageFactor && this.deque.length > 16) {
            this.halfSize(this.deque.length);
        }
    }

    /**
     * Removes the first element in Deque and returns it.
     *
     * @return
     */
    public T removeFirst() {
        if (this.size == 0) {
            return null;
        }
        int nextIndex = this.newFirst + 1;
        T firstItem = this.deque[nextIndex];
        this.deque[nextIndex] = null;
        this.newFirst = nextIndex;
        this.size -= 1;
        this.reduce();
        return firstItem;
    }

    /**
     * Removes the last element in Deque and returns it.
     *
     * @return
     */
    public T removeLast() {
        if (this.size == 0) {
            return null;
        }
        int prevIndex = this.newLast - 1;
        T lastItem = this.deque[prevIndex];
        this.deque[prevIndex] = null;
        this.newLast = prevIndex;
        this.size -= 1;
        this.reduce();
        return lastItem;
    }

    /**
     * Returns the item at the specified index if it exists.
     *
     * @param index index wanted.
     * @return
     */
    public T get(int index) {
        if (index >= this.size || index < 0) {
            return null;
        }
        return this.deque[index];
    }

}
