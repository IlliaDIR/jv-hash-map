package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;
    private static final int ZERO = 0;

    private Node<K,V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
        this.size = ZERO;
        this.threshold = (int) (capacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(hash(key));
        Node<K,V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hash(key), key, value);
            size++;
        } else {
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                } else {
                    if (currentNode.next != null) {
                        currentNode = currentNode.next;
                    } else {
                        currentNode.next = new Node<>(hash(key), key, value);
                        size++;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(hash(key));
        Node<K,V> current = table[index];
        while (current != null) {
            if (key == null) {
                if (current.key == null) {
                    return current.value;
                }
            } else {
                if (key.equals(current.key)) {
                    return current.value;
                }
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(int hash) {
        return hash & (capacity - 1);
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private Node<K,V>[] resize() {
        capacity *= GROW_FACTOR;
        Node<K,V>[] newTable = (Node<K,V>[]) new Node[capacity];
        for (Node<K,V> node : table) {
            Node<K,V> current = node;
            while (current != null) {
                Node<K,V> next = current.next;
                int newIndex = getIndex(current.hash);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        table = newTable;
        threshold = calculateThreshold();
        return table;
    }

    private int calculateThreshold() {
        return (int) (capacity * LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
