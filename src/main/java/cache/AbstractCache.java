package cache;

import key.Key;
import key.TLKey;

import java.util.concurrent.*;

public class AbstractCache<K, V> {
    protected ConcurrentHashMap<Key, V> cacheMap;
    //private long DEFAULT_CHECK_PERIOD = 862000000;
    protected int size;

    protected static final int DEFAULT_SIZE = 100;

    private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread th = new Thread(r);
            th.setDaemon(true);
            return th;
        }
    });

    public AbstractCache(int size)  throws Exception {
        //cacheMap = new ConcurrentHashMap<>();
        this.size = size;
    }

    public AbstractCache()  throws Exception {
        this(DEFAULT_SIZE);
    }

}
