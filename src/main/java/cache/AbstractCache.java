package cache;

import key.Key;
import java.io.*;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public abstract class AbstractCache<K, V> implements Serializable {
    protected ConcurrentHashMap<Key, V> cacheMap;
    protected PriorityQueue<Key> priorityQueue;
    protected int size;
    protected ObjectInputStream inputStream;
    protected ObjectOutputStream outputStream;

    protected static final int DEFAULT_SIZE = 100;

    public AbstractCache(int size) throws Exception {
        this.cacheMap = new ConcurrentHashMap<Key, V>();
        this.size = size;
    }

    public AbstractCache() throws Exception {
        this(DEFAULT_SIZE);
    }

    /**
     * Class to compare Keys by priority
     */
    protected static Comparator<Key> priorityComparator = new Comparator<Key>() {
        @Override
        public int compare(Key key1, Key key2) {
            return Long.compare(key1.getPriority(), key2.getPriority());
        }
    };

    /**
     * Class to compare Keys by priority in reverse order
     */
    protected static Comparator<Key> reversePriorityComparator = new Comparator<Key>() {
        @Override
        public int compare(Key key1, Key key2) {
            return -Long.compare(key1.getPriority(), key2.getPriority());
        }
    };

    public abstract boolean put(K key, V data) throws IOException;
    public abstract V get(K key) throws IOException, ClassNotFoundException;
    public abstract void remove(K key);

    /**
     * Remove all objects from cache
     */
    public void removeAll(){
        priorityQueue.clear();
        cacheMap.clear();
    }

    /**
     * Add new data in cache
     * Lifetime is setting by default
     *
     * @param map map with new data
     */
    public void addAll(Map<K, V> map) throws IOException{
        for (Map.Entry<K, V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
