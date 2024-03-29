package cache;

import key.Key;
import key.RUKey;
import java.util.PriorityQueue;

/**
 * Cache follows the MRU Cache Policy
 */
public class MRUCache<K, V> extends AbstractCache<K, V>{

    public MRUCache(int size) throws Exception {
        super(size);
        this.priorityQueue = new PriorityQueue<Key>(reversePriorityComparator);
    }

    public MRUCache() throws Exception {
        this(DEFAULT_SIZE);
    }

    /**
     * Insert an object into the cache
     * @param key     key of an object in the cache
     * @param data    data contained by the object in the cache
     */
    @Override
    public boolean put(K key, V data) {
        Key addedKey = new RUKey(key);
        if(!cacheMap.containsKey(addedKey)){
            if (cacheMap.size() == size) {
                Key removingKey = priorityQueue.poll();
                assert removingKey != null;
                cacheMap.remove(removingKey);
            }
            cacheMap.put(addedKey, data);
            priorityQueue.add(addedKey);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Getting an object from cache by key
     * @param key key of an object in the cache
     * @return data object from the cache
     */
    @Override
    public V get(K key) {
        RUKey newKey = new RUKey(key, System.currentTimeMillis());
        if (cacheMap.containsKey(newKey)) {
            priorityQueue.remove(newKey);
            priorityQueue.add(newKey);
            return cacheMap.get(newKey);
        }else{
            return null;
        }
    }

    /**
     * Remove objects from cache by key
     * @param key key of an object in the cache
     */
    @Override
    public void remove(K key) {
        RUKey removingKey = new RUKey(key);
        priorityQueue.remove(removingKey);
        cacheMap.remove(removingKey);
    }
}
