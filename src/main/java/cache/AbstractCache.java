package cache;

import key.Key;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.*;

//TODO: add saving on disk
public abstract class AbstractCache<K, V> {
    protected ConcurrentHashMap<Key, V> cacheMap;
    protected PriorityQueue<Key> priorityQueue; //if it isn't working for threads - use PriorityBlockingQueue
    protected int size;

    protected static final int DEFAULT_SIZE = 100;

    public AbstractCache(int size)  throws Exception {
        this.cacheMap = new ConcurrentHashMap<Key, V>();
        this.size = size;
    }

    public AbstractCache()  throws Exception {
        this(DEFAULT_SIZE);
    }

    /**
     * Class to compare Keys by time priority
     */
    protected static Comparator<Key> priorityComparator = new Comparator<Key>() {
        @Override
        public int compare(Key key1, Key key2) {
            return Long.compare(key1.getPriority(), key2.getPriority());
        }
    };

    protected static Comparator<Key> reversePriorityComparator = new Comparator<Key>() {
        @Override
        public int compare(Key key1, Key key2) {
            return -Long.compare(key1.getPriority(), key2.getPriority());
        }
    };

    public abstract boolean put(K key, V data);
    public abstract V get(K key);
    public abstract void remove(K key);
    public abstract void removeAll();
    public abstract void addAll(Map<K, V> map);
}
