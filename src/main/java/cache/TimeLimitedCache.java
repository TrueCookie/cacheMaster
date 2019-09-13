package cache;

import elements.Key;
import java.util.Map;
import java.util.concurrent.*;

public class TimeLimitedCache<K, V> {
    private ConcurrentHashMap<Key, V> cacheMap = new ConcurrentHashMap<>();
    private static final long DEFAULT_TIMEOUT = 36000000;
    private long timeout;
    private ScheduledExecutorService cacheExecutor = Executors.newSingleThreadScheduledExecutor(/*new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread th = new Thread(r);
            th.setDaemon(true);
            return th;
        }
    }*/);

    public TimeLimitedCache()  throws Exception {
        new TimeLimitedCache(DEFAULT_TIMEOUT);
    }

    /** Cache with defined objects lifetime
     * @param timeout number of milliseconds - time of keeping objects in cache
     */
    public TimeLimitedCache(long timeout)  throws Exception {
        if (timeout < 100) {    //what's the difference between this timeout and deathTime in cache object?
            throw new Exception("Too short interval for storage in the cache. Interval should be more than 10 ms");
        }
        this.timeout = timeout;
        cacheExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                for (Key key : cacheMap.keySet()) { //where is the shedule cache call(debug)
                    if (!key.isLive(currentTime)) {
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
        return cacheMap.get(new Key(key));
    }

    /**
     * Remove objects from cache by key
     * @param key - ключ
     */
    public void remove(K key) {
        cacheMap.remove(new Key(key));
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
        ConcurrentHashMap<Key, V> tmpMap = new ConcurrentHashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            tmpMap.put(new Key(entry.getKey(), timeout), entry.getValue());
        }
        cacheMap = tmpMap;
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