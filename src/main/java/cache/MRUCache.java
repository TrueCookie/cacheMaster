package cache;

import key.RUKey;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache follows the MRU Cache Policy
 */
public class MRUCache<K, V> extends AbstractCache<K, V>{
    private ConcurrentHashMap<RUKey, V> cacheMap;  //<? extends Key, V>
    private PriorityQueue<RUKey> timePriorityQueue;   //if it isn't working for threads - use PriorityBlockingQueue

    public MRUCache(int size) throws Exception {
        super(size);
        this.cacheMap = new ConcurrentHashMap<RUKey, V>();
        this.timePriorityQueue = new PriorityQueue<RUKey>(MRUCacheComparator);
    }

    public MRUCache() throws Exception {
        super();
    }

    /**
     * Class to compare Keys by time priority
     */
    public static Comparator<RUKey> MRUCacheComparator = new Comparator<RUKey>() {
        @Override
        public int compare(RUKey key1, RUKey key2) {
            return -Long.compare(key1.getPriority(), key2.getPriority());
        }
    };

    /**
     * Method to insert an object into the cache
     * @param key     key of the object in the cache
     * @param data    data contained by the object in the cache
     */
    @Override
    public boolean put(K key, V data) {
        RUKey addedKey = new RUKey(key);
        if(!cacheMap.containsKey(addedKey)){
            if (cacheMap.size() == size) {
                RUKey removingKey = timePriorityQueue.poll();
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
     * Getting an object from cache by key
     * @param key key of the object in the cache
     * @return data object from the cache
     */
    @Override
    public V get(K key) {
        RUKey newKey = new RUKey(key, System.currentTimeMillis());
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
        RUKey removingKey = new RUKey(key);
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
