package deque;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque {

    private Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comp = c;
    }

    private List<T> getDeque() {
        int size = super.size();
        List<T> deque = new ArrayList<T>();
        for (int i = 0; i < size; i++) {
            deque.add((T) super.get(i));
        }
        return deque;
    }

    public T max() {
        if (super.isEmpty()) {
            return null;
        }
        List<T> deque = getDeque();
        return Collections.max(deque, comp);
    }

    public T max(Comparator<T> c) {
        if (super.isEmpty()) {
            return null;
        }
        List<T> deque = getDeque();
        return Collections.max(deque, c);
    }

    public static void main(String[]args){
//
//        MaxArrayDeque<Integer> MAD = new MaxArrayDeque<>();

    }

}
