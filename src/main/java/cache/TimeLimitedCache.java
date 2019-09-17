package cache;

import key.TLKey;
import java.util.Map;
import java.util.concurrent.*;

//TODO: make the cache check itself once it's full
//not once some period
public class TimeLimitedCache<K, V> {
    private ConcurrentHashMap<TLKey, V> cacheMap;
    private static final long DEFAULT_LIFETIME = 86200000;
    private long lifetime;

    /** Cache with defined objects lifetime
     * @param lifetime number of milliseconds - time of keeping objects in cache
     */
    public TimeLimitedCache(long lifetime)  throws Exception {
        if (lifetime < 100) {
            throw new Exception("Too short interval for storage in the cache. Interval should be more than 10 ms");
        }
        cacheMap = new ConcurrentHashMap<>();
        this.lifetime = lifetime;
    }

    public TimeLimitedCache()  throws Exception {
        this(DEFAULT_LIFETIME);
    }

    public void runCacheExecutor(){
        //long checkPeriod = this.lifetime/500;
        ScheduledExecutorService cacheExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread th = new Thread(runnable);
                th.setDaemon(true);
                return th;
            }
        });
        cacheExecutor.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            for (TLKey key : cacheMap.keySet()) {
                if (!key.isLive(currentTime)) {
                    cacheMap.remove(key);
                }
            }
        }, 1, 500, TimeUnit.MILLISECONDS);  //raw checkPeriod is available here
    }


    /**
     * @param timeout number of milliseconds object keeping in cache
     */
    public void setDefaultLifetime(long timeout) throws Exception {
        if (timeout < 100) {
            throw new Exception("Too short interval for storage in the cache. Interval should be more than 10 ms");
        }
        this.lifetime = timeout;
    }

    /**
     * Method to insert an object into the cache
     * Lifetime is setting by default
     * @param key key of the object in the cache
     * @param data data contained by the object in the cache
     */
    public void put(K key, V data) {
        cacheMap.put(new TLKey(key, lifetime), data);
    }

    /**
     * Method to insert an object into the cache
     * @param key key of the object in the cache
     * @param data data contained by the object in the cache
     * @param timeout number of milliseconds object keeping in cache
     */
    public void put(K key, V data, long timeout) {
        cacheMap.put(new TLKey(key, timeout), data);
    }

    /**
     * Getting an object from cache by key
     * @param key key of the object in the cache
     * @return data object from the cache
     */
    public V get(K key) {
        return cacheMap.get(new TLKey(key));
    }

    /**
     * Remove objects from cache by key
     * @param key - ключ
     */
    public void remove(K key) {
        cacheMap.remove(new TLKey(key));
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
        ConcurrentHashMap<TLKey, V> tmpMap = new ConcurrentHashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            tmpMap.put(new TLKey(entry.getKey(), lifetime), entry.getValue());    //at what point the lifetime turn into 0
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
