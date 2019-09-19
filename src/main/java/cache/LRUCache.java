package cache;

import key.Key;
import key.LRUKey;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache follows the LRU Cache Policy
 */
public class LRUCache<K, V> extends AbstractCache<K, V> {
    private ConcurrentHashMap<LRUKey, V> cacheMap;  //<? extends Key, V>
    private PriorityQueue<LRUKey> timePriorityQueue;   //if it isn't working for threads - use PriorityBlockingQueue

    public LRUCache(int size) throws Exception {
        super(size);
        this.cacheMap = new ConcurrentHashMap<LRUKey, V>();
        this.timePriorityQueue = new PriorityQueue<LRUKey>(cacheComparator);
    }

    public LRUCache() throws Exception {
        super(DEFAULT_SIZE);
    }

    /**
     * Method to insert an object into the cache
     * @param key     key of the object in the cache
     * @param data    data contained by the object in the cache
     */
    @Override
    public boolean put(K key, V data) {
        LRUKey addedKey = new LRUKey(key);
        if(!cacheMap.containsKey(addedKey)){
            if (cacheMap.size() == size) {
                LRUKey removingKey = timePriorityQueue.poll();
                assert removingKey != null;     //TODO: read about assert
                cacheMap.remove(removingKey);
            }
            cacheMap.put(addedKey, data);
            timePriorityQueue.add(addedKey);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Class to compare Keys by time priority
     */
    /*public static Comparator<LRUKey> cacheComparator = new Comparator<LRUKey>() {
        @Override
        public int compare(LRUKey key1, LRUKey key2) {
            return Long.compare(key1.getPriority(), key2.getPriority());
        }
    };*/

    /**
     * Getting an object from cache by key
     * @param key key of the object in the cache
     * @return data object from the cache
     */
    @Override
    public V get(K key) {
        LRUKey newKey = new LRUKey(key, System.currentTimeMillis());
        if (cacheMap.containsKey(newKey)) {
            timePriorityQueue.remove(newKey);
            timePriorityQueue.add(newKey);
            return cacheMap.get(newKey);
        }else{
            return null;
        }
    }

    /**
     * Remove objects from cache by key
     * @param key - ключ
     */
    @Override
    public void remove(K key) {
        LRUKey removingKey = new LRUKey(key);
        timePriorityQueue.remove(removingKey);
        cacheMap.remove(removingKey);
    }

    /**
     * Remove all objects from cache
     */
    @Override
    public void removeAll() {
        timePriorityQueue.clear();
        cacheMap.clear();
    }

    /**
     * Add new data in cache
     * Lifetime is setting by default
     * @param map map with new data
     */
    @Override
    public void addAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

}
