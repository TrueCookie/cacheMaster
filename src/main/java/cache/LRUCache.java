package cache;

import key.Key;
import key.RUKey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Cache follows the LRU Cache Policy
 */
public class LRUCache<K, V> extends AbstractCache<K, V> {
    private File cacheFile;
    private FileOutputStream fileOutputStream;

    public LRUCache(int size) throws Exception {
        super(size);
        this.priorityQueue = new PriorityQueue<Key>(priorityComparator);
    }

    public LRUCache(int size, File cacheFile) throws Exception {
        super(size);
        this.priorityQueue = new PriorityQueue<Key>(priorityComparator);
        this.cacheFile = cacheFile;
        this.fileOutputStream = new FileOutputStream(cacheFile);
    }

    public LRUCache() throws Exception {
        this(DEFAULT_SIZE);
    }

    public void writeOnDisk() throws IOException {  //should i put data straight into the file or put just map&queue
        fileOutputStream.write(testStr.getBytes());
        fileOutputStream.write(testInt1);

        fileOutputStream.close();
    }

    /**
     * Method to insert an object into the cache
     *
     * @param key  key of the object in the cache
     * @param data data contained by the object in the cache
     */
    @Override
    public boolean put(K key, V data) {
        Key addedKey = new RUKey(key);
        if (!cacheMap.containsKey(addedKey)) {
            if (cacheMap.size() == size) {
                Key removingKey = priorityQueue.poll();
                assert removingKey != null;
                cacheMap.remove(removingKey);
            }
            cacheMap.put(addedKey, data);
            priorityQueue.add(addedKey);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Getting an object from cache by key
     *
     * @param key key of the object in the cache
     * @return data object from the cache
     */
    @Override
    public V get(K key) {
        RUKey newKey = new RUKey(key, System.currentTimeMillis());
        if (cacheMap.containsKey(newKey)) {
            priorityQueue.remove(newKey);
            priorityQueue.add(newKey);
            return cacheMap.get(newKey);
        } else {
            return null;
        }
    }

    /**
     * Remove objects from cache by key
     *
     * @param key - ключ
     */
    @Override
    public void remove(K key) {
        RUKey removingKey = new RUKey(key);
        priorityQueue.remove(removingKey);
        cacheMap.remove(removingKey);
    }

    /**
     * Remove all objects from cache
     */
    @Override
    public void removeAll() {
        priorityQueue.clear();
        cacheMap.clear();
    }

    /**
     * Add new data in cache
     * Lifetime is setting by default
     *
     * @param map map with new data
     */
    @Override
    public void addAll(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

}
