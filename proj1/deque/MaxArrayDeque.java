package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    /**
     * Default Comparator for Deque.
     */
    private Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comp = c;
    }

    /**
     * Gets the Deque from the parent.
     *
     * @return returns the Deque.
     */
    private T[] getDeque() {
        int size = super.size();
        T[] deque = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            deque[i] = (T) super.get(i);
        }
        return deque;
    }

    /**
     * Returns the max dictated by the default Comparator.
     *
     * @return
     */
    public T max() {
        if (super.isEmpty()) {
            return null;
        }
        T[] deque = getDeque();
        int maxDex = 0;
        for (int i = 1; i < super.size(); i++) {
            if (comp.compare(deque[i], deque[maxDex]) > 0) {
                maxDex = i;
            }
        }
        return deque[maxDex];
    }

    /**
     * Returns the max dictated by the specified Comparator.
     *
     * @param c Comparator to use.
     * @return
     */
    public T max(Comparator<T> c) {
        if (super.isEmpty()) {
            return null;
        }
        T[] deque = getDeque();
        int maxDex = 0;
        for (int i = 1; i < super.size(); i++) {
            if (c.compare(deque[i], deque[maxDex]) > 0) {
                maxDex = i;
            }
        }
        return deque[maxDex];
    }

}
