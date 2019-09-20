package cache;

import key.Key;
import key.LFUKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LFUCache<K, V> extends AbstractCache<K, V> {
    private ConcurrentHashMap<Key, V> cacheMap;  //<? extends Key, V>
    private PriorityQueue<LFUKey> priorityQueue;   //if it isn't working for threads - use PriorityBlockingQueue

    public LFUCache(int size) throws Exception {
        super(size);
        this.cacheMap = new ConcurrentHashMap<Key, V>();
        this.priorityQueue = new PriorityQueue<LFUKey>(LRUCacheComparator);
    }

    public static Comparator<LFUKey> LRUCacheComparator = new Comparator<LFUKey>() {
        @Override
        public int compare(LFUKey key1, LFUKey key2) {
            return Long.compare(key1.getPriority(), key2.getPriority());
        }
    };

    @Override
    public boolean put(K key, V data) {
        LFUKey addedKey = new LFUKey(key);
        if(!cacheMap.containsKey(addedKey)){
            if (cacheMap.size() == size) {
                LFUKey removingKey = priorityQueue.poll();
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
     * There i should getPriority from LFUKey from queue or map.
     *
     * @param key
     * @return
     */
    @Override
    public V get(K key) {
        LFUKey newKey = new LFUKey(key);
        if (cacheMap.containsKey(newKey)) {
            //newKey = new LFUKey(key, getKeyPriority(newKey)+1);
            priorityQueue.remove(newKey);
            newKey = new LFUKey(key, getKeyPriority(newKey, cacheMap)+1);
            priorityQueue.add(newKey);
            V oldValue = cacheMap.get(newKey);  //& now i should update key in cacheMap
            cacheMap.remove(newKey);
            cacheMap.put(newKey, oldValue);
            return cacheMap.get(newKey);
        }else{
            return null;
        }
    }

    private long getKeyPriority(LFUKey targetKey, ConcurrentHashMap<Key,V> map){  //find oldKey by newKey with key
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

    @Override
    public void removeAll() {
        priorityQueue.clear();
        cacheMap.clear();
    }

    @Override
    public void addAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
