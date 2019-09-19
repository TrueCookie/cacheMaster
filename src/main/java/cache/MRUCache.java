package cache;

import java.util.Map;

public class MRUCache extends AbstractCache{

    public MRUCache(int size) throws Exception {
        super(size);
    }

    @Override
    public boolean put(Object key, Object data) {
        return false;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public void remove(Object key) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void addAll(Map map) {

    }
}
