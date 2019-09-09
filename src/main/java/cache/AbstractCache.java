package cache;

import elements.Key;

import java.util.concurrent.*;

public class AbstractCache<K, V> {
    private ConcurrentHashMap<Key, V> cacheMap = new ConcurrentHashMap<>();
    private long DEFAULT_CHECK_PERIOD = 862000000;
    private ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread th = new Thread(r);
            th.setDaemon(true);
            return th;
        }
    });

    public AbstractCache()  throws Exception {
        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                for (Key key : cacheMap.keySet()) {
                    if (!key.isLive(currentTime)) {
                        cacheMap.remove(key);
                    }
                }
            }
        }, 1, DEFAULT_CHECK_PERIOD, TimeUnit.MILLISECONDS);
    }

}
