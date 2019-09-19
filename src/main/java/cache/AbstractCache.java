package cache;

import key.Key;

import java.util.concurrent.*;

//TODO: add saving on disk
public class AbstractCache<K, V> {
    protected ConcurrentHashMap<Key, V> cacheMap;
    protected int size;

    protected static final int DEFAULT_SIZE = 100;

    public AbstractCache(int size)  throws Exception {
        this.size = size;
    }

    public AbstractCache()  throws Exception {
        this(DEFAULT_SIZE);
    }

}
