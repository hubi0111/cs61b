package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private int capacity = 16;
    private double loadFactor = 0.75;
    private int size = 0;

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /**
     * Constructors
     */
    public MyHashMap() {
        buckets = (Collection<Node>[]) new Collection[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        capacity = initialSize;
        buckets = (Collection<Node>[]) new Collection[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        capacity = initialSize;
        loadFactor = maxLoad;
        buckets = (Collection<Node>[]) new Collection[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return null;
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return null;
    }

    @Override
    public void clear() {
        buckets = (Collection<Node>[]) new Collection[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = createBucket();
        }
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        for (Collection<Node> c : buckets) {
            for (Node n : c) {
                if (n.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        for (Collection<Node> c : buckets) {
            for (Node n : c) {
                if (n.key.equals(key)) {
                    return n.value;
                }
            }
        }
        size++;
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (containsKey(key)) {
            for (Collection<Node> c : buckets) {
                for (Node n : c) {
                    if (n.key.equals(key)) {
                        n.value = value;
                        return;
                    }
                }
            }
        } else {
            int hashCode = value.hashCode() % capacity;
            buckets[hashCode].add(new Node(key, value));
            size++;
        }
        if (size / capacity >= loadFactor) {
            Collection<Node>[] copy = (Collection<Node>[]) new Collection[capacity];
            capacity *= 2;
            System.arraycopy(buckets, 0, copy, 0, size);
            clear();
            for (Collection<Node> c : copy) {
                for (Node n : c) {
                    put(n.key, n.value);
                }
            }
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Collection<Node> c : buckets) {
            for (Node n : c) {
                keySet.add(n.key);
            }
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

}
