package cache;

import key.Key;
import key.TLKey;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/** Cache follows the LRU Cache Policy
 */
public class LRUCache<K, V> extends AbstractCache {
    private ConcurrentHashMap<TLKey, V> cacheMap;
    private PriorityQueue<TLKey> timePriorityQueue;   //if it isn't working for threads - use PriorityBlockingQueue
    private Integer maxSize;

    public LRUCache() throws Exception {
        cacheMap = new ConcurrentHashMap<>();
    }
    public void runCacheExecutor(){
        Thread cacheExecutor = new Thread(){
            public void run(){
                long currentTime = System.currentTimeMillis();
                if (cacheMap.size() == maxSize) {
                    //timePriorityQueue.peek();
                    TLKey removingKey = timePriorityQueue.extractMinPreorityValue();
                    //cacheMap.remove(removingKey);
                }
                for (TLKey key : cacheMap.keySet()) {
                    if (!key.isLive(currentTime)) {
                        cacheMap.remove(key);
                    }
                }
            }
        };
    }

    /**
     * Method to insert an object into the cache
     * Lifetime is setting by default
     * @param key key of the object in the cache
     * @param data data contained by the object in the cache
     */
    /*public void put(K key, V data) {
        cacheMap.put(new Key(key, lifetime), data);
    }*/

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
     * Class to compare Keys by priority
     */
    public static Comparator<Key> cacheComparator = new Comparator<Key>(){
        @Override
        public int compare(Key key1, Key key2) {    //it should be cacheMap objects
            return Long.compare(key1.getPriority(), key2.getPriority());
        }
    };

    /**
     * Getting an object from cache by key
     * @param key key of the object in the cache
     * @return data object from the cache
     */
    public V get(K key) {
        long currentTime = System.currentTimeMillis();
        if (cacheMap.containsKey(key)) {
            // Сначала обновим время последнего запроса к key
            timePriorityQueue.set(key, currentTime);    //TODO: create or find method to set object existing in queue
            return cacheMap.get(timePriorityQueue.peek());
        }
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
    /*public void setAll(Map<K, V> map) {
        ConcurrentHashMap<Key, V> tmpMap = new ConcurrentHashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            tmpMap.put(new Key(entry.getKey(), lifetime), entry.getValue());    //at what point the lifetime turn into 0
        }
        cacheMap = tmpMap;
    }*/

    /**
     * Add new data in cache
     * Lifetime is setting by default
     * @param map map with new data
     */
    /*public void addAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }*/

}
