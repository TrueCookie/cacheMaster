package cache;

import elements.Key;

import java.util.concurrent.ConcurrentHashMap;

public class AbstractCache<K, V> {
    private ConcurrentHashMap<Key, V> cacheMap = new ConcurrentHashMap<>();
    public AbstractCache()  throws Exception {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                for (Key key : cacheMap.keySet()) {
                    if (!key.isLive(currentTime)) {
                        cacheMap.remove(key);
                    }
                }
            }
        }, 1, timeout/5, TimeUnit.MILLISECONDS);
    }

}
