public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] hashTable;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
        threshold = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node(key, value);
        int index = hashFunction(key);
        if (hashTable[index] == null) {
            hashTable[index] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = hashTable[index];
        for (; ; ) {
            if (newNode.key == currentNode.key || newNode.key != null
                    && newNode.key.equals(currentNode.key)) {
                currentNode.value = value;
                break;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                break;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = hashFunction(key);
        Node<K, V> currentNode = hashTable[index];
        while (currentNode != null) {
            if ((key == null && currentNode.key == null)
                    || key != null && key.equals(currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashFunction(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode() % hashTable.length;
        if (hash < 0) {
            hash = hash * (-1);
        }
        return hash;
    }

    private void resize() {
        if (size >= threshold * DEFAULT_LOAD_FACTOR) {
            threshold = threshold * 2;
            if (size >= hashTable.length * DEFAULT_LOAD_FACTOR) {
                Node<K, V>[] oldBuckets = hashTable;
                // hashTable = new Node[threshold];
                hashTable = new Node[hashTable.length * 2];
                size = 0;
                for (Node<K, V> node : oldBuckets) {
                    while (node != null) {
                        put(node.key, node.value);
                        node = node.next;
                    }
                }
            }
        }
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}

