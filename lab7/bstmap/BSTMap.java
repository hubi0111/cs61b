package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int size = 0;
    private Node<K, V> root = null;

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        return containsKey(key, root);
    }

    private boolean containsKey(K key, Node n) {
        if (n == null) {
            return false;
        }
        if (n.key.equals(key)) {
            return true;
        } else {
            return containsKey(key, n.left) || containsKey(key, n.right);
        }
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return null;
        }
        return get(key, root);
    }

    private V get(K key, Node<K, V> n) {
        if (n == null) {
            return null;
        }
        if (n.key.compareTo(key) == 0) {
            return n.value;
        } else if (n.key.compareTo(key) > 0) {
            return get(key, n.left);
        } else {
            return get(key, n.right);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
        } else {
            put(key, value, root);
        }
        size++;
    }

    private Node<K, V> put(K key, V value, Node<K, V> n) {
        if (n == null) {
            return new Node(key, value);
        }
        if (n.key.compareTo(key) == 0) {
            n.value = value;
        } else if (n.key.compareTo(key) > 0) {
            n.left = put(key, value, n.left);
        } else {
            n.right = put(key, value, n.right);
        }
        return n;
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node<K, V> n) {
        if (n == null) {
            return;
        }
        printInOrder(n.left);
        System.out.print(n.key);
        printInOrder(n.right);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }
}
