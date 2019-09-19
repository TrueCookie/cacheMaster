package key;

import java.util.Objects;

public class LRUKey extends Key{
    private long lastRequestTime;

    public LRUKey(Object key, long requestTime) {
        super(key);
        lastRequestTime = requestTime;
    }

    public LRUKey(Object key) {
        super(key);
        lastRequestTime = 0;
    }

    @Override
    public long getPriority(){
        return lastRequestTime;
    }

    @Override
    public String toString() {
        return "Key{" + "key=" + key + ", requestTime=" + lastRequestTime + "}";
    }

}
