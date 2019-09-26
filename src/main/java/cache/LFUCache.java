package cache;

import key.Key;
import key.LFUKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache follows the LFU Cache Policy
 */
public class LFUCache<K, V> extends AbstractCache<K, V> {

    public LFUCache(int size) throws Exception {
        super(size);
        this.priorityQueue = new PriorityQueue<Key>(priorityComparator);
    }

    public LFUCache() throws Exception {
        this(DEFAULT_SIZE);
    }

    @Override
    public boolean put(K key, V data) {
        Key addedKey = new LFUKey(key);
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
     * @param key key for value in a cache
     * @return
     */
    @Override
    public V get(K key) {
        LFUKey newKey = new LFUKey(key);
        if (cacheMap.containsKey(newKey)) {
            priorityQueue.remove(newKey);
            newKey = new LFUKey(key, getKeyPriority(newKey, cacheMap)+1);
            priorityQueue.add(newKey);
            V oldValue = cacheMap.get(newKey);
            cacheMap.remove(newKey);
            cacheMap.put(newKey, oldValue);
            return cacheMap.get(newKey);
        }else{
            return null;
        }
    }

    private long getKeyPriority(LFUKey targetKey, ConcurrentHashMap<Key,V> map){
        ConcurrentHashMap<Key,V> tmpMap = new ConcurrentHashMap<Key,V>(map);
        Long priority = null;
        for (Map.Entry<Key, V> entry : tmpMap.entrySet()) {
            if(entry.getKey().equals(targetKey)){
                priority = entry.getKey().getPriority();
                break;
            }
        }
        return priority;
    }

    @Override
    public void remove(K key) {
        LFUKey removingKey = new LFUKey(key);
        priorityQueue.remove(removingKey);
        cacheMap.remove(removingKey);
    }
}
