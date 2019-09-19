package cache;

import key.Key;
import key.TLKey;

import java.util.concurrent.*;

//TODO: add saving on disk
public class AbstractCache<K, V> {
    protected ConcurrentHashMap<Key, V> cacheMap;
    protected int size;

    protected static final int DEFAULT_SIZE = 100;

    /*private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread th = new Thread(r);
            th.setDaemon(true);
            return th;
        }
    });*/

    public AbstractCache(int size)  throws Exception {
        //cacheMap = new ConcurrentHashMap<>();
        this.size = size;
    }

    public AbstractCache()  throws Exception {
        this(DEFAULT_SIZE);
    }

}
