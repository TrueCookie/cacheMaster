package cache;

import key.Key;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.*;

//TODO: add saving on disk
//TODO: replace priorityQueue with a priorityQueue with pairs <key, priority>
public abstract class AbstractCache<K, V> {
    protected ConcurrentHashMap<Key, V> cacheMap;
    protected PriorityQueue<Key> priorityQueue; //if it isn't working for threads - use PriorityBlockingQueue
    protected int size;

    protected static final int DEFAULT_SIZE = 100;

    public AbstractCache(int size)  throws Exception {
        this.cacheMap = new ConcurrentHashMap<Key, V>();
        this.size = size;
    }

    public AbstractCache()  throws Exception {
        this(DEFAULT_SIZE);
    }

    /**
     * Class to compare Keys by time priority
     */
    protected static Comparator<Key> priorityComparator = new Comparator<Key>() {
        @Override
        public int compare(Key key1, Key key2) {
            return Long.compare(key1.getPriority(), key2.getPriority());
        }
    };

    protected static Comparator<Key> reversePriorityComparator = new Comparator<Key>() {
        @Override
        public int compare(Key key1, Key key2) {
            return -Long.compare(key1.getPriority(), key2.getPriority());
        }
    };

    public abstract boolean put(K key, V data);
    public abstract V get(K key);
    public abstract void remove(K key);
    public abstract void removeAll();
    public abstract void addAll(Map<K, V> map);

/*    public void writeOnDisk() throws IOException {
        File file = new File("C:"+File.separator+"Cache"+File.separator+"cache");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        String testStr = "SOME CACHE DATA HERE";
        Integer testInt1 = 600606;
        Object testObj2 = "(/¯◡ ‿ ◡)/¯ ~ ┻━┻";
        Object testObj3 = 8+"ಠ_ಠ";

        fileOutputStream.write(testStr.getBytes());
        fileOutputStream.write(testInt1);

        fileOutputStream.close();
    }*/
}
