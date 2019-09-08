package cache;

import elements.Key;

import java.util.concurrent.ConcurrentHashMap;

public class AbstractCache<K, V> {
    private ConcurrentHashMap<Key, V> cacheMap = new ConcurrentHashMap<>();
}
