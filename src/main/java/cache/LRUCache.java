package cache;

import key.Key;
import key.RUKey;

import java.io.*;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache follows the LRU Cache Policy
 */
public class LRUCache<K, V> extends AbstractCache<K, V> implements Serializable {
    private File cacheFile;
    private boolean writeOnDiskFlag;

    public LRUCache(int size) throws Exception {
        super(size);
        this.priorityQueue = new PriorityQueue<Key>(priorityComparator);
        writeOnDiskFlag = false;
    }

    public LRUCache(int size, File cacheFile) throws Exception {
        super(size);
        this.priorityQueue = new PriorityQueue<Key>(priorityComparator);
        this.cacheFile = cacheFile;
        File dir = cacheFile.getParentFile();
        if (!dir.exists()){
            dir.mkdirs();
        }
        if (!cacheFile.exists() && !cacheFile.isDirectory()){
            cacheFile.createNewFile();
        }
        writeOnDiskFlag = true;
    }

    public LRUCache() throws Exception {
        this(DEFAULT_SIZE);
    }

    /**
     * Method to insert an object into the cache
     *
     * @param key key of an object in the cache
     * @param data data contained by the object in the cache
     */
    @Override
    public boolean put(K key, V data) throws IOException {
        Key addedKey = new RUKey(key);
        if (!cacheMap.containsKey(addedKey)) {
            if (cacheMap.size() == size) {
                Key removingKey = priorityQueue.poll();
                assert removingKey != null;
                cacheMap.remove(removingKey);
            }
            cacheMap.put(addedKey, data);
            priorityQueue.add(addedKey);
            if(writeOnDiskFlag){
                this.outputStream = new ObjectOutputStream(new FileOutputStream(cacheFile));
                outputStream.writeObject(cacheMap);
                this.outputStream.close();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Getting an object from cache by key
     *
     * @param key key of an object in the cache
     * @return data object from the cache
     */
    @SuppressWarnings("unchecked")
    @Override
    public V get(K key) throws IOException, ClassNotFoundException {
        RUKey newKey = new RUKey(key, System.currentTimeMillis());
        if(writeOnDiskFlag) {
            this.inputStream = new ObjectInputStream(new FileInputStream(cacheFile));
            Object inputObj = inputStream.readObject();
            if(inputObj instanceof ConcurrentHashMap){
                this.cacheMap = (ConcurrentHashMap<Key, V>) inputObj;
            }
            this.inputStream.close();
        }
        if (cacheMap.containsKey(newKey)) {
            priorityQueue.remove(newKey);
            priorityQueue.add(newKey);
            if(writeOnDiskFlag){
                this.outputStream = new ObjectOutputStream(new FileOutputStream(cacheFile));
                outputStream.writeObject(cacheMap);
                this.outputStream.close();
            }
            return cacheMap.get(newKey);
        } else {
            return null;
        }
    }

    /**
     * Remove objects from cache by key
     *
     * @param key key of an object in the cache
     */
    @Override
    public void remove(K key) {
        RUKey removingKey = new RUKey(key);
        priorityQueue.remove(removingKey);
        cacheMap.remove(removingKey);
    }

}
