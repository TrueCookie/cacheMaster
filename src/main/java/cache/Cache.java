package cache;

import elements.Key;
import java.util.Map;
import java.util.concurrent.*;

public class Cache<K, V> {
    private ConcurrentHashMap<Key, V> cacheMap = new ConcurrentHashMap<>();
    private static long DEFAULT_TIMEOUT = 36000000;
    private long timeout;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread th = new Thread(r);
            th.setDaemon(true);
            return th;
        }
    });

    public Cache()  throws Exception {
        new Cache(DEFAULT_TIMEOUT);
    }

    /** Cache with defined objects lifetime
     * @param timeout number of milliseconds - time of keeping objects in cache
     */
    public Cache(long timeout)  throws Exception {
        if (timeout < 100) {
            throw new Exception("Too short interval for storage in the cache. Interval should be more than 10 ms");
        }
        this.timeout = timeout;
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                long current = System.currentTimeMillis();
                for (Key key : cacheMap.keySet()) {
                    if (!key.isLive(current)) {
                        cacheMap.remove(key);
                    }
                }
            }
        }, 1, timeout/5, TimeUnit.MILLISECONDS);
    }


    /**
     * @param timeout number of milliseconds object keeping in cache
     */
    public void setDefault_timeout(long timeout) throws Exception {
        if (timeout < 100) {
            throw new Exception("Too short interval for storage in the cache. Interval should be more than 10 ms");
        }
        this.timeout = timeout;
    }

    /**
     * Method to insert an object into the cache
     * Lifetime is setting by default
     * @param key key of the object in the cache
     * @param data data contained by the object in the cache
     */
    public void put(K key, V data) {
        cacheMap.put(new Key(key, timeout), data);
    }

    /**
     * Method to insert an object into the cache
     * @param key key of the object in the cache
     * @param data data contained by the object in the cache
     * @param timeout number of milliseconds object keeping in cache
     */
    public void put(K key, V data, long timeout) {
        cacheMap.put(new Key(key, timeout), data);
    }

    /**
     * Getting an object from cache by key
     * @param key key of the object in the cache
     * @return data object from the cache
     */
    public V get(K key) {
        return cacheMap.get(new Key(key, timeout)); //Key(key)
    }

    /**
     * Remove objects from cache by key
     * @param key - ключ
     */
    public void remove(K key) {
        cacheMap.remove(new Key(key, timeout)); //Key(key)
    }

    /**
     * Remove all objects from cache
     */
    public void removeAll() {
        cacheMap.clear();
    }

    /**
     * Replace all the cache by recieved map
     * Lifetime is setting by default
     * @param map map with new data
     */
    public void setAll(Map<K, V> map) {
        ConcurrentHashMap tempmap = new ConcurrentHashMap<Key, V>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            tempmap.put(new Key(entry.getKey(), timeout), entry.getValue());
        }
        cacheMap = tempmap;
    }

    /**
     * Add new data in cache
     * Lifetime is setting by default
     * @param map map with new data
     */
    public void addAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

}
